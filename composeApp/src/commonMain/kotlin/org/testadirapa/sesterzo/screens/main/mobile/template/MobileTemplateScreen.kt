package org.testadirapa.sesterzo.screens.main.mobile.template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.components.template.MobileSourceUpdateForm
import org.testadirapa.sesterzo.components.template.TemplateStatsCard
import org.testadirapa.sesterzo.components.template.TemplateUpdateMenu
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.model.Space
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.add_source_edit
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
	var templatesOrNull by remember { mutableStateOf<Templates?>(null) }
	var showSheet by remember { mutableStateOf(false) }
	val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	var templateToUpdate by remember { mutableStateOf<Pair<String, DecryptedBudgetElement>?>(null) }
	LaunchedEffect(space.id) {
		runCatching {
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
			templatesOrNull = Templates(
				expensesTemplate = expensesTemplate,
				savingsTemplate = savingsTemplate,
				incomesTemplate = incomesTemplate,
			)
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
				}
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
			templateToUpdate?.let { (title, budget) ->
				MobileSourceUpdateForm(
					title = title,
					type = stringResource(Res.string.add_source_type_template),
					sources = budget.elements
				)
			}
		}
	}
}

@Composable
private fun TemplateTitle() {
	Column {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp),
		) {
			Icon(
				painter = painterResource(Res.drawable.cycle),
				tint = colorScheme.primary,
				contentDescription = null,
				modifier = Modifier.size(12.dp)
			)
			Text(
				text = stringResource(Res.string.template_page_subtitle),
				style = MaterialTheme.typography.titleSmall,
				fontWeight = FontWeight.Bold,
				color = colorScheme.primary,
			)
		}
		Text(
			text = stringResource(Res.string.template_page_title),
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.Bold,
			color = colorScheme.onBackground,
		)
	}
}