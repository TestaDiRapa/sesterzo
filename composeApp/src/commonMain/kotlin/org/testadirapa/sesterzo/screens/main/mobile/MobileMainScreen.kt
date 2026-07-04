package org.testadirapa.sesterzo.screens.main.mobile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.testadirapa.sesterzo.components.mobile.main.BottomMenu
import org.testadirapa.sesterzo.components.mobile.main.HeaderBar
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.screens.main.mobile.budget.MobileBudgetScreen
import org.testadirapa.sesterzo.screens.main.mobile.settings.SettingsMobilePage
import org.testadirapa.sesterzo.screens.main.mobile.template.MobileTemplateScreen
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.viewmodel.BudgetViewModel

enum class Page { Budget, Entries, Template, Settings }

@Composable
fun MobileMainScreen(
	space: Space,
	initialPage: Page,
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
	onCreateSpace: (Space) -> Unit,
	onSwitchSpace: (Space, Base64String?) -> Unit,
) {
	var currentPage by remember { mutableStateOf(initialPage) }

	Scaffold(
		topBar = {
			HeaderBar(
				space = space,
				onCreateSpace = onCreateSpace,
				spaceThumbnail = spaceThumbnail,
				onSwitchSpace = onSwitchSpace,
				onError = onError,
			)
		},
		bottomBar = {
			BottomMenu(
				currentPage = currentPage,
				onPageChange = { currentPage = it },
			)
		},
	) { innerPadding ->
		Column(
			modifier = Modifier
				.padding(paddingValues = innerPadding)
				.padding(horizontal = 16.dp)
				.fillMaxSize()
		) {
			budgetView?.let { budgets ->
				when(currentPage) {
					Page.Budget, Page.Entries -> {
						MobileBudgetScreen(
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
						MobileTemplateScreen(
							space = space,
							onUpdateBudgetsTemplate = onUpdateBudgetsTemplate,
							onError = onError,
						)
					}
					Page.Settings -> {
						SettingsMobilePage(
							space = space,
							spaceThumbnail = spaceThumbnail,
							onError = onError,
							onSpaceUpdate = onSpaceUpdate,
						)
					}
				}
			}
		}
	}
}