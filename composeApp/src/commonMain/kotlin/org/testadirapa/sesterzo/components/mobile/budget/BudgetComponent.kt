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
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.DecryptedEntry
import org.testadirapa.sesterzo.model.Timestamp

@Composable
fun BudgetComponent(
	entries: List<DecryptedEntry>,
) {
	Column {
		entries.forEach {
			Text("${it.label} - ${it.amount} - ${it.updated}")
		}
	}
}