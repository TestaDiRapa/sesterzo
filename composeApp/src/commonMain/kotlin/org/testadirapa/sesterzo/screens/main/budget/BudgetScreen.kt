package org.testadirapa.sesterzo.screens.main.budget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.Timestamp

@Composable
fun BudgetScreen(
	refreshKey: Timestamp
) {
	var scope = rememberCoroutineScope()
	var currentBudget by remember { mutableStateOf<DecryptedBudget?>(null) }
	LaunchedEffect(Unit) {

	}
	LaunchedEffect(refreshKey) {

	}
}