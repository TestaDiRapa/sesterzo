package org.testadirapa.sesterzo.screens.main.mobile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.testadirapa.sesterzo.screens.main.mobile.budget.MobileBudgetScreen

@Composable
fun MobileMainScreen(
	initialSpaceId: String,
	onError: (e: Throwable) -> Unit,
) {
	var spaceId by remember { mutableStateOf(initialSpaceId) }
	MobileBudgetScreen(
		spaceId = spaceId,
		onError = onError,
	)
}