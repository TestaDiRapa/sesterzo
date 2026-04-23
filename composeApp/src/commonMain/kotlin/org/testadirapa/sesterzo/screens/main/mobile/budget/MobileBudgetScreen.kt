package org.testadirapa.sesterzo.screens.main.mobile.budget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.testadirapa.sesterzo.components.mobile.budget.BudgetComponent
import org.testadirapa.sesterzo.components.mobile.budget.BudgetMonthSelector
import org.testadirapa.sesterzo.utils.currentDate
import kotlin.time.Clock

@Composable
fun MobileBudgetScreen(
	spaceId: String,
) {
	var selectedMonth by remember { mutableStateOf(currentDate()) }
	Scaffold { innerPadding ->
		Column(
			modifier = Modifier
				.padding(paddingValues = innerPadding)
				.padding(horizontal = 16.dp)
				.fillMaxSize()
		) {
			BudgetMonthSelector(
				date = selectedMonth,
				onDateClick = {},
				onPrev = null,
				onNext = null,
				onCreate = {}
			)
			BudgetComponent(
				refreshKey = Clock.System.now().toEpochMilliseconds(),
				spaceId = spaceId,
				budgetDate = selectedMonth,
			)
		}
	}
}