package org.testadirapa.sesterzo.screens.main.desktop

import androidx.compose.runtime.Composable
import org.testadirapa.sesterzo.components.desktop.DesktopNavBar
import org.testadirapa.sesterzo.components.scaffold.DesktopScaffold
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.model.Space
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
	onCreateSpace: (Space) -> Unit,
	onSwitchSpace: (Space) -> Unit,
) {
	DesktopScaffold(
		navBar = {
			DesktopNavBar(
				space = space,
				spaceThumbnail = spaceThumbnail,
				onError = onError,
			)
		},
		topBar = {},
		mainContent = {}
	)
}