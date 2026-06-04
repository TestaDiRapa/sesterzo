package org.testadirapa.sesterzo.components.mobile.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.testadirapa.sesterzo.components.mobile.space.MobileSpaceSwitcher
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Space
import kotlin.time.Clock

@Composable
fun HeaderBar(
	space: Space,
	spaceThumbnail: Base64String?,
	onCreateSpace: (Space) -> Unit,
	onSwitchSpace: (Space) -> Unit,
	onError: (Throwable) -> Unit
) {
	var sheetOpen by remember { mutableStateOf(false) }
	var openKey by remember { mutableStateOf(0L) }

	Column {
		Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))
		Row(
			modifier = Modifier
				.padding(horizontal = 16.dp)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
		) {
			SpaceMenuBadge(
				space = space,
				spaceThumbnail = spaceThumbnail,
				onClick = {
					openKey = Clock.System.now().toEpochMilliseconds()
					sheetOpen = true
				},
			)
		}
		Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))

		if (sheetOpen) {
			MobileSpaceSwitcher(
				activeId = space.id,
				openKey = openKey,
				onSelect = {
					onSwitchSpace(it)
					sheetOpen = false
				},
				onCreate = { onCreateSpace(space) },
				onJoin = {},
				onDismiss = { sheetOpen = false },
				onError = onError
			)
		}
	}

}