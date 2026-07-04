package org.testadirapa.sesterzo.screens.main.desktop.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Space

@Composable
fun DesktopSettingsScreen(
	space: Space,
	spaceThumbnail: Base64String?,
	onSpaceUpdate: (space: Space, thumbnail: Base64String?) -> Unit,
	onError: (e: Throwable) -> Unit
) {
	Row(
		modifier = Modifier.fillMaxSize(),
	) {
		Column(
			modifier = Modifier.weight(1f)
		) {

		}
		VerticalDivider()
		Column(
			modifier = Modifier.weight(2f).fillMaxHeight(),
		) {

		}
	}
}