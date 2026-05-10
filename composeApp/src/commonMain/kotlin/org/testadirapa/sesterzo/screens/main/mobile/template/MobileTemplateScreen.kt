package org.testadirapa.sesterzo.screens.main.mobile.template

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.App
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.components.mobile.template.MobileSourceUpdateForm
import org.testadirapa.sesterzo.components.template.TemplateStatsCard
import org.testadirapa.sesterzo.components.template.TemplateUpdateMenu
import org.testadirapa.sesterzo.components.text.TextWithIcon
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.model.toReference
import org.testadirapa.sesterzo.utils.currentBudgetReference
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.add_source_type_template
import sesterzo.composeapp.generated.resources.cycle
import sesterzo.composeapp.generated.resources.template_page_subtitle
import sesterzo.composeapp.generated.resources.template_page_title

private data class Templates(
	val expensesTemplate: DecryptedBudgetElement,
	val savingsTemplate: DecryptedBudgetElement,
	val incomesTemplate: DecryptedBudgetElement,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobileTemplateScreen(
	space: Space,
	onError: (Throwable) -> Unit,
) {
	val scope = rememberCoroutineScope()
	var loadingState by remember { mutableStateOf(false) }
	var templatesOrNull by remember { mutableStateOf<Templates?>(null) }
	var showSheet by remember { mutableStateOf(false) }
	val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	var templateToUpdate by remember { mutableStateOf<Pair<String, DecryptedBudgetElement>?>(null) }
	LaunchedEffect(space.id) {
		runCatching {
			templatesOrNull = retrieveLatestTemplates(space)
		}.onFailure(onError)
	}

	Column {
		templatesOrNull?.let { templates ->
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
		} ?: run {

		}
	}

	if (showSheet) {
		ModalBottomSheet(
			onDismissRequest = { showSheet = false },
			sheetState = sheetState,
			containerColor = colorScheme.surface,
		) {
			templateToUpdate?.let { (title, template) ->
				MobileSourceUpdateForm(
					title = title,
					type = stringResource(Res.string.add_source_type_template),
					sources = template.elements,
					entity = template,
					loadingState = loadingState,
					onSourceUpdate = { entity, updatedAmounts, updatedCurrentBudget ->
						loadingState = true
						scope.launch {
							runCatching {
								val updatedElement = AppCtx.api.budgetElement.createBudgetElement(
									spaceId = space.id,
									budgetElement = entity.copy(
										version = entity.version + 1,
										elements = updatedAmounts
									)
								)
								if (updatedCurrentBudget) {
									AppCtx.api.budget.updateBudgetTemplate(
										spaceId = space.id,
										budgetReference = currentBudgetReference(),
										type = updatedElement.type,
										budgetElementReference = updatedElement.toReference(),
									)
								}
							}.onFailure(onError)
							runCatching {
								templatesOrNull = retrieveLatestTemplates(space)
							}.onFailure(onError)
						}
						showSheet = false
						loadingState = false
					}
				)
			}
		}
	}
}

@Composable
private fun TemplateTitle() {
	Column {
		TextWithIcon(
			icon = painterResource(Res.drawable.cycle),
			text = stringResource(Res.string.template_page_subtitle),
			color = colorScheme.primary,
		)
		Text(
			text = stringResource(Res.string.template_page_title),
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.Bold,
			color = colorScheme.onBackground,
		)
	}
}

private suspend fun retrieveLatestTemplates(space: Space): Templates {
	val expensesTemplate = AppCtx.api.budgetElement.getLatestBudgetElementById(
		spaceId = space.id,
		budgetElementId = space.fixedExpensesTemplateId
	)
	val savingsTemplate = AppCtx.api.budgetElement.getLatestBudgetElementById(
		spaceId = space.id,
		budgetElementId = space.savingsTemplateId
	)
	val incomesTemplate = AppCtx.api.budgetElement.getLatestBudgetElementById(
		spaceId = space.id,
		budgetElementId = space.incomeSourcesTemplateId
	)
	return Templates(
		expensesTemplate = expensesTemplate,
		savingsTemplate = savingsTemplate,
		incomesTemplate = incomesTemplate,
	)
}