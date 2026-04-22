package org.testadirapa.sesterzo.screens.main.mobile

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
import org.testadirapa.sesterzo.components.mobile.budget.BudgetSelector
import org.testadirapa.sesterzo.utils.currentDate

@Composable
fun MobileMainScreen(
	initialSpaceId: String,
) {
	var spaceId by remember { mutableStateOf(initialSpaceId) }
	Scaffold { innerPadding ->
		Column(
			modifier = Modifier
				.padding(paddingValues = innerPadding)
				.padding(horizontal = 16.dp)
				.fillMaxSize()
		) {
			BudgetSelector(
				date = currentDate(),
				onDateClick = {}
			)
		}
	}
}