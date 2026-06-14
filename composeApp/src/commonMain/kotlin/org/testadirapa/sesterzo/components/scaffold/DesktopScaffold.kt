package org.testadirapa.sesterzo.components.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DesktopScaffold(
	navBar: @Composable () -> Unit,
	topBar: @Composable () -> Unit,
	mainContent: @Composable () -> Unit,
) {
	Row(
		modifier = Modifier.fillMaxSize(),
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
		) {
			Box(
				modifier = Modifier.weight(1f)
			) {
				navBar()
			}
			Column(
				modifier = Modifier.weight(4f)
			) {
				topBar()
				mainContent()
			}
		}

	}
}