package org.testadirapa.sesterzo.components.scaffold

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DesktopTopBarScaffold(
	headerComponent: @Composable () -> Unit,
	bodyComponent: @Composable () -> Unit,
) {
	Column(
		modifier = Modifier.fillMaxSize()
	) {
		headerComponent()
		HorizontalDivider(color = colorScheme.outline)
		bodyComponent()
	}
}