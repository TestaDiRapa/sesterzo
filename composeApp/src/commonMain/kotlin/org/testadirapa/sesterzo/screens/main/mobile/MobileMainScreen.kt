package org.testadirapa.sesterzo.screens.main.mobile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.ktor.util.date.GMTDate
import org.testadirapa.sesterzo.screens.main.mobile.budget.BudgetScreen
import kotlin.time.Clock

@Composable
fun MobileMainScreen(
	initialSpaceId: String,
) {
	var spaceId by remember { mutableStateOf(initialSpaceId) }
	BudgetScreen(
		refreshKey = Clock.System.now().toEpochMilliseconds(),
		spaceId = spaceId,
		budgetDate = GMTDate()
	)
}