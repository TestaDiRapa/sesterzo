package org.testadirapa.sesterzo.screens.main.mobile

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.testadirapa.sesterzo.components.mobile.main.HeaderBar
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.screens.main.mobile.budget.MobileBudgetScreen

@Composable
fun MobileMainScreen(
	initialSpace: Space,
	onError: (e: Throwable) -> Unit,
	onCreateSpace: (Space) -> Unit,
) {
	var space by remember { mutableStateOf(initialSpace) }
	Column {
		HeaderBar(
			space = space,
			onCreateSpace = onCreateSpace,
			onSwitchSpace = {
				space = it
			},
			onError = onError,
		)
		MobileBudgetScreen(
			spaceId = space.id,
			onError = onError,
		)
	}
}