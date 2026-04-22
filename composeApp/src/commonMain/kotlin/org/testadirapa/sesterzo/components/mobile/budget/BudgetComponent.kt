package org.testadirapa.sesterzo.components.mobile.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.datetime.LocalDate
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.DecryptedExpense
import org.testadirapa.sesterzo.model.Timestamp

@Composable
fun BudgetComponent(
	refreshKey: Timestamp,
	spaceId: String,
	budgetDate: LocalDate
) {
	val scope = rememberCoroutineScope()
	var budget by remember { mutableStateOf<DecryptedBudget?>(null) }
	var expenses by remember { mutableStateOf<List<DecryptedExpense>>(emptyList()) }
	LaunchedEffect(Unit) {
		val loadedBudget =  AppCtx.api.budget.getOrCreateMonthBudget(
			spaceId = spaceId,
			budgetDate = budgetDate,
			bypassCache = false // TODO
		)
		expenses = AppCtx.api.expense.getInSpaceForBudget(
			spaceId = spaceId,
			budgetId = loadedBudget.id,
			bypassCache = false // TODO
		)
		budget = loadedBudget
	}
	LaunchedEffect(refreshKey) {
		budget?.id?.also { budgetId ->
			expenses = AppCtx.api.expense.getInSpaceForBudget(
				spaceId = spaceId,
				budgetId = budgetId,
				bypassCache = false // TODO
			)
		}
	}
	Scaffold { scaffoldPadding ->
		Column {
			expenses.forEach {
				Text("${it.label} - ${it.amount} - ${it.updated}")
			}
		}
	}
}