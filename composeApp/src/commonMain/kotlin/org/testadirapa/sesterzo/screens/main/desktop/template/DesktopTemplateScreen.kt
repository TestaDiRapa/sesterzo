package org.testadirapa.sesterzo.screens.main.desktop.template

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import org.testadirapa.sesterzo.components.desktop.template.TemplateTopBar
import org.testadirapa.sesterzo.components.scaffold.DesktopTopBarScaffold
import org.testadirapa.sesterzo.components.template.TemplateStatsCard
import org.testadirapa.sesterzo.components.template.TemplateTitle
import org.testadirapa.sesterzo.components.template.TemplateUpdateMenu
import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.viewmodel.components.TemplateScreenViewModel

@Composable
fun DesktopTemplateScreen(
	space: Space,
	onUpdateBudgetsTemplate: (type: BudgetElement.BudgetElementType, budgetElement: DecryptedBudgetElement, updateCurrent: Boolean) -> Unit,
	onError: (Throwable) -> Unit,
) {
	val viewModel = viewModel(key = space.id) {
		TemplateScreenViewModel(space = space, errorHandler = onError)
	}
	val templatesOrNull = viewModel.templatesStates.collectAsState()
	val loadingState = viewModel.loadingState.collectAsState()
	var templateToUpdate by remember { mutableStateOf<Pair<String, DecryptedBudgetElement>?>(null) }
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
				modifier = Modifier.weight(2f),
			) {

			}
		}
	}
}