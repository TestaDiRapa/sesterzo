package org.testadirapa.sesterzo.screens.main.mobile.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.testadirapa.sesterzo.components.mobile.budget.BudgetComponent
import org.testadirapa.sesterzo.model.Timestamp
import org.testadirapa.sesterzo.utils.currentDate

@Composable
fun BudgetScreen(
	refreshKey: Timestamp,
	spaceId: String,
) {
	var selectedMonth by remember { mutableStateOf(currentDate()) }
	Scaffold { innerPadding ->
		Column {
			BudgetComponent(
				refreshKey = refreshKey,
				spaceId = spaceId,
				budgetDate = selectedMonth,
			)
		}
	}
}