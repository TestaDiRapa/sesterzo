package org.testadirapa.sesterzo.screens.main.mobile.template

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.template.SourceUpdateForm
import org.testadirapa.sesterzo.components.template.TemplateStatsCard
import org.testadirapa.sesterzo.components.template.TemplateTitle
import org.testadirapa.sesterzo.components.template.TemplateUpdateMenu
import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.viewmodel.components.TemplateScreenViewModel
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.add_source_type_template

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobileTemplateScreen(
	space: Space,
	onUpdateBudgetsTemplate: (type: BudgetElement.BudgetElementType, budgetElement: DecryptedBudgetElement, updateCurrent: Boolean) -> Unit,
	onError: (Throwable) -> Unit,
) {
	val viewModel = viewModel(key = space.id) {
		TemplateScreenViewModel(space = space, errorHandler = onError)
	}
	val templatesOrNull = viewModel.templatesStates.collectAsState()
	val loadingState = viewModel.loadingState.collectAsState()

	var showSheet by remember { mutableStateOf(false) }
	val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	var templateToUpdate by remember { mutableStateOf<Pair<String, DecryptedBudgetElement>?>(null) }

	Column {
		templatesOrNull.value?.let { templates ->
			TemplateTitle()
			Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
			TemplateStatsCard(
				incomeSources = templates.incomesTemplate,
				savings = templates.savingsTemplate,
				expenses = templates.expensesTemplate,
			)
			Spacer(modifier = Modifier.fillMaxWidth().height(16.dp))
			TemplateUpdateMenu(
				incomeSources = templates.incomesTemplate,
				savings = templates.savingsTemplate,
				expenses = templates.expensesTemplate,
				onFormOpen = { showSheet = !showSheet },
				setTemplate = {
					templateToUpdate = it
				},
			)
		}
	}

	if (showSheet) {
		ModalBottomSheet(
			onDismissRequest = { showSheet = false },
			sheetState = sheetState,
			containerColor = colorScheme.surface,
		) {
			templateToUpdate?.let { (title, template) ->
				SourceUpdateForm(
					title = title,
					type = stringResource(Res.string.add_source_type_template),
					sources = template.elements,
					entity = template,
					loadingState = loadingState.value,
					onSourceUpdate = { entity, updatedAmounts, updatedCurrentBudget ->
						viewModel.acceptIntent(
							TemplateScreenViewModel.TemplateScreenIntent.UpdateTemplate(
								entity = entity,
								updatedAmounts = updatedAmounts,
								updatedCurrentBudget = updatedCurrentBudget,
								onUpdateBudgetsTemplate = onUpdateBudgetsTemplate,
							)
						)
						showSheet = false
					}
				)
			}
		}
	}
}