package org.testadirapa.sesterzo.screens.main.desktop.template

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.desktop.template.TemplateTopBar
import org.testadirapa.sesterzo.components.scaffold.DesktopTopBarScaffold
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

@Composable
fun DesktopTemplateScreen(
	space: Space,
	onUpdateBudgetsTemplate: (type: BudgetElement.BudgetElementType, budgetElement: DecryptedBudgetElement, updateCurrent: Boolean) -> Unit,
	onError: (Throwable) -> Unit,
) {
	val viewModel = viewModel(key = space.id) {
		TemplateScreenViewModel(space = space, errorHandler = onError)
	}
	var templateToUpdate by remember { mutableStateOf<Pair<String, DecryptedBudgetElement>?>(null) }
	val templatesOrNull = viewModel.templatesStates.collectAsState()
	val loadingState = viewModel.loadingState.collectAsState()
	DesktopTopBarScaffold(
		headerComponent = {
			TemplateTopBar()
		}
	) {
		Row(
			modifier = Modifier.fillMaxSize(),
		) {
			Column(
				modifier = Modifier.weight(1f).padding(all = 16.dp),
			) {
				templatesOrNull.value?.let { templates ->
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
						onFormOpen = { },
						setTemplate = {
							templateToUpdate = it
						},
					)
				}
			}
			VerticalDivider()
			Column(
				modifier = Modifier.weight(2f).fillMaxHeight(),
			) {
				templateToUpdate?.let { (title, template) ->
					val templates = templatesOrNull.value
					when {
						template.budgetElementId == templates?.savingsTemplate?.budgetElementId &&
							template.version != templates.savingsTemplate.version -> {
								templateToUpdate = title to templates.savingsTemplate
							}
						template.budgetElementId == templates?.expensesTemplate?.budgetElementId &&
							template.version != templates.expensesTemplate.version -> {
							templateToUpdate = title to templates.expensesTemplate
						}
						template.budgetElementId == templates?.incomesTemplate?.budgetElementId &&
							template.version != templates.incomesTemplate.version -> {
							templateToUpdate = title to templates.incomesTemplate
						}
					}
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
						}
					)
				}
			}
		}
	}
}