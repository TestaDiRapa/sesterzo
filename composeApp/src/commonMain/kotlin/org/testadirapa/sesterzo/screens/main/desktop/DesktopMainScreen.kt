package org.testadirapa.sesterzo.screens.main.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.testadirapa.sesterzo.components.desktop.DesktopNavBar
import org.testadirapa.sesterzo.components.scaffold.DesktopScaffold
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.screens.main.desktop.budget.DesktopBudgetEntryScreen
import org.testadirapa.sesterzo.screens.main.desktop.settings.DesktopSettingsScreen
import org.testadirapa.sesterzo.screens.main.desktop.template.DesktopTemplateScreen
import org.testadirapa.sesterzo.screens.main.mobile.Page
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.viewmodel.BudgetViewModel

@Composable
fun DesktopMainScreen(
	space: Space,
	spaceThumbnail: Base64String?,
	budgetView: BudgetViewModel.BudgetView?,
	loadingState: Boolean,
	onPreviousBudget: () -> Unit,
	onNextBudget: () -> Unit,
	onMonthSelect: (BudgetReference) -> Unit,
	onCreateBudget: (BudgetReference) -> Unit,
	onBudgetUpdate: (budget: DecryptedBudget, newAmounts: Map<String, Amount>, type: Entry.EntryType) -> Unit,
	onUpdateBudgetsTemplate: (type: BudgetElement.BudgetElementType, budgetElement: DecryptedBudgetElement, updateCurrent: Boolean) -> Unit,
	onSpaceUpdate: (space: Space, thumbnail: Base64String?) -> Unit,
	onError: (e: Throwable) -> Unit,
	onCreateSpace: (currentSpace: Space) -> Unit,
	onSwitchSpace: (Space) -> Unit,
) {

	var currentPage by remember { mutableStateOf(Page.Budget) }

	DesktopScaffold(
		navBar = {
			DesktopNavBar(
				space = space,
				spaceThumbnail = spaceThumbnail,
				currentPage = currentPage,
				onCreateSpace = onCreateSpace,
				onSwitchSpace = onSwitchSpace,
				onPageChange = { currentPage = it },
				onError = onError,
			)
		},
		mainContent = {
			budgetView?.let { budgets ->
				when (currentPage) {
					Page.Budget, Page.Entries -> {
						DesktopBudgetEntryScreen(
							space = space,
							page = currentPage,
							budget = budgets.currentBudget,
							budgetLoadingState = loadingState,
							onPreviousBudget = budgets.previousBudget?.let { onPreviousBudget },
							onNextBudget = budgets.nextBudget?.let { onNextBudget },
							onMonthSelect = onMonthSelect,
							onCreateBudget = onCreateBudget,
							onBudgetUpdate = onBudgetUpdate,
							onError = onError,
						)
					}
					Page.Template -> {
						DesktopTemplateScreen(
							space = space,
							onUpdateBudgetsTemplate = onUpdateBudgetsTemplate,
							onError = onError,
						)
					}
					Page.Settings -> {
						DesktopSettingsScreen(
							space = space,
							spaceThumbnail = spaceThumbnail,
							onError = onError,
							onSpaceUpdate = onSpaceUpdate,
						)
					}
				}
			}
		}
	)
}