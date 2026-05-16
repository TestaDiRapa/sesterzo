package org.testadirapa.sesterzo.screens.main.mobile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.testadirapa.sesterzo.components.mobile.entries.AddEntryButtonWithForm
import org.testadirapa.sesterzo.components.mobile.main.BottomMenu
import org.testadirapa.sesterzo.components.mobile.main.HeaderBar
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.screens.main.mobile.budget.MobileBudgetScreen
import org.testadirapa.sesterzo.screens.main.mobile.template.MobileTemplateScreen
import org.testadirapa.sesterzo.utils.toReference
import org.testadirapa.sesterzo.viewmodel.SpaceViewModel
import org.testadirapa.sesterzo.viewmodel.intents.SpaceIntent

enum class Page { Budget, Template }

@Composable
fun MobileMainScreen(
	initialSpace: Space,
	onError: (e: Throwable) -> Unit,
	onCreateSpace: (Space) -> Unit,
) {
	var currentPage by remember { mutableStateOf(Page.Budget) }
	var space by remember { mutableStateOf(initialSpace) }
	val viewModel = viewModel(key = "${space.id}-budget") {
		SpaceViewModel(spaceId = space.id, errorHandler = onError)
	}
	val budgetView = viewModel.budgetViewState.collectAsState()
	val entries = viewModel.entriesViewState.collectAsState()
	val loadingState = viewModel.loadingState.collectAsState()
	Scaffold(
		topBar = {
			HeaderBar(
				space = space,
				onCreateSpace = onCreateSpace,
				onSwitchSpace = { space = it },
				onError = onError,
			)
		},
		bottomBar = {
			BottomMenu(
				currentPage = currentPage,
				onPageChange = { currentPage = it },
			)
		},
		floatingActionButton = {
			budgetView.value?.let {
				AddEntryButtonWithForm(
					space = space,
					currentBudget = it.currentBudget.toReference(),
					loadingState = loadingState.value,
					onCreate = { budgetReference, type, label, amount, description ->
						viewModel.acceptIntent(
							SpaceIntent.CreateEntry(
								budgetReference = budgetReference,
								type = type,
								label = label,
								amount = amount,
								description = description,
							)
						)
					}
				)
			}
		},
		floatingActionButtonPosition = FabPosition.End,
	) { innerPadding ->
		Column(
			modifier = Modifier
				.padding(paddingValues = innerPadding)
				.padding(horizontal = 16.dp)
				.fillMaxSize()
		) {
			when(currentPage) {
				Page.Budget -> {
					MobileBudgetScreen(
						spaceId = space.id,
						budgetView = budgetView.value,
						onNavigateToPrevious = { viewModel.acceptIntent(SpaceIntent.NavigateToPrevious) },
						onNavigateToNext = { viewModel.acceptIntent(SpaceIntent.NavigateToNext) },
						onCreate = { reference -> viewModel.acceptIntent(SpaceIntent.CreateBudget(reference)) },
						onSelect = { reference -> viewModel.acceptIntent(SpaceIntent.NavigateTo(reference))},
						entries = entries.value,
						onError = onError,
					)
				}
				Page.Template -> {
					MobileTemplateScreen(
						space = space,
						onError = onError,
					)
				}
			}
		}
	}
}