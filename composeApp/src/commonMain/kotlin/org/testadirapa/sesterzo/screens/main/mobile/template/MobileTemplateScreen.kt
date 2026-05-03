package org.testadirapa.sesterzo.screens.main.mobile.template

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.components.template.TemplateStatsCard
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.model.Space

private data class Templates(
	val expensesTemplate: DecryptedBudgetElement,
	val savingsTemplate: DecryptedBudgetElement,
	val incomesTemplate: DecryptedBudgetElement,
)

@Composable
fun MobileTemplateScreen(
	space: Space,
	onError: (Throwable) -> Unit,
) {
	var templatesOrNull by remember { mutableStateOf<Templates?>(null) }
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
	templatesOrNull?.let { templates ->
		TemplateStatsCard(
			incomeSources = templates.incomesTemplate,
			savings = templates.savingsTemplate,
			expenses = templates.expensesTemplate,
		)
	} ?: run {

	}
}