package org.testadirapa.sesterzo.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.screens.main.desktop.DesktopMainScreen
import org.testadirapa.sesterzo.screens.main.mobile.MobileMainScreen
import org.testadirapa.sesterzo.screens.main.mobile.Page
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.viewmodel.BudgetViewModel
import org.testadirapa.sesterzo.viewmodel.intents.BudgetIntent

@Composable
fun MainScreen(
	isMobile: Boolean,
	initialSpace: Space,
	initialPage: Page,
	onError: (e: Throwable) -> Unit,
	onCreateSpace: (Space) -> Unit,
) {
	val scope = rememberCoroutineScope()
	LaunchedEffect(Unit) {
		val currentUser = AppCtx.api.user.getCurrentUser()
		AppCtx.currency = currentUser.preferredCurrency
		AppCtx.sendErrors = currentUser.sendLogs
	}

	var space by remember { mutableStateOf(initialSpace) }
	var spaceThumbnail by remember { mutableStateOf<Base64String?>(null) }
	val viewModel = viewModel(key = "${space.id}-budget") {
		BudgetViewModel(spaceId = space.id, errorHandler = onError)
	}
	val budgetView = viewModel.budgetViewState.collectAsState()
	val loadingState = viewModel.loadingState.collectAsState()

	LaunchedEffect(initialSpace.pictureReference) {
		initialSpace.pictureReference?.also {
			runCatching {
				spaceThumbnail = AppCtx.api.attachment.getAttachmentInSpace(
					spaceId = space.id,
					attachmentId = it,
					bypassCache = false
				)?.data
			}.onFailure(onError)
		}
	}

	val onPreviousBudget = { viewModel.acceptIntent(BudgetIntent.NavigateToPrevious) }
	val onNextBudget = { viewModel.acceptIntent(BudgetIntent.NavigateToNext) }
	val onMonthSelect = { reference: BudgetReference ->
		viewModel.acceptIntent(BudgetIntent.NavigateTo(reference))
	}
	val onCreateBudget = { reference: BudgetReference ->
		viewModel.acceptIntent(BudgetIntent.CreateBudget(reference))
	}
	val onBudgetUpdate = { budget: DecryptedBudget, newAmounts: Map<String, Amount>, type: Entry.EntryType ->
		viewModel.acceptIntent(BudgetIntent.UpdateBudgetAmount(budget, newAmounts, type))
	}
	val onUpdateBudgetsTemplate = { type: BudgetElement.BudgetElementType, budgetElement: DecryptedBudgetElement, updateCurrentBudget: Boolean ->
		viewModel.acceptIntent(
			BudgetIntent.UpdateCurrentBudgetTemplate(type, budgetElement, updateCurrentBudget)
		)
	}
	val onSpaceUpdate = { updatedSpace: Space, updatedThumbnail: Base64String? ->
		space = updatedSpace
		spaceThumbnail = updatedThumbnail
	}
	val onSwitchSpace = { it: Space, thumbnail: Base64String? ->
		scope.launch {
			AppCtx.propertyRepository.setDefaultSpace(it.id)
		}
		space = it
		spaceThumbnail = thumbnail
	}

	if (isMobile) {
		MobileMainScreen(
			space = space,
			initialPage = initialPage,
			spaceThumbnail = spaceThumbnail,
			budgetView = budgetView.value,
			loadingState = loadingState.value,
			onPreviousBudget = onPreviousBudget,
			onNextBudget = onNextBudget,
			onMonthSelect = onMonthSelect,
			onCreateBudget = onCreateBudget,
			onBudgetUpdate = onBudgetUpdate,
			onUpdateBudgetsTemplate = onUpdateBudgetsTemplate,
			onSpaceUpdate = onSpaceUpdate,
			onError = onError,
			onCreateSpace = onCreateSpace,
			onSwitchSpace = onSwitchSpace,
		)
	} else {
		DesktopMainScreen(
			space = space,
			initialPage = initialPage,
			spaceThumbnail = spaceThumbnail,
			budgetView = budgetView.value,
			loadingState = loadingState.value,
			onPreviousBudget = onPreviousBudget,
			onNextBudget = onNextBudget,
			onMonthSelect = onMonthSelect,
			onCreateBudget = onCreateBudget,
			onBudgetUpdate = onBudgetUpdate,
			onUpdateBudgetsTemplate = onUpdateBudgetsTemplate,
			onSpaceUpdate = onSpaceUpdate,
			onError = onError,
			onCreateSpace = onCreateSpace,
			onSwitchSpace = onSwitchSpace,
		)
	}
}