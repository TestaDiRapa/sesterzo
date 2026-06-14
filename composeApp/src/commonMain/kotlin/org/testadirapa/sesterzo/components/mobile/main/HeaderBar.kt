package org.testadirapa.sesterzo.components.mobile.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.testadirapa.sesterzo.components.mobile.space.MobileSpaceSwitcher
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.viewmodel.components.SpaceSwitcherViewModel

@Composable
fun HeaderBar(
	space: Space,
	spaceThumbnail: Base64String?,
	onCreateSpace: (currentSpace: Space) -> Unit,
	onSwitchSpace: (Space) -> Unit,
	onError: (Throwable) -> Unit
) {
	val viewModel = viewModel { SpaceSwitcherViewModel(errorHandler = onError) }
	var sheetOpen by remember { mutableStateOf(false) }
	val isLoading = viewModel.loadingState.collectAsState()
	val spaces = viewModel.spacesState.collectAsState()
	val spaceThumbnails = viewModel.spaceThumbnailsState.collectAsState()

	Column(modifier = Modifier.statusBarsPadding()) {
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
					sheetOpen = true
					viewModel.acceptIntent(SpaceSwitcherViewModel.SpaceSwitcherIntent.RefreshSpaces)
				},
			)
		}
		Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))

		if (sheetOpen) {
			MobileSpaceSwitcher(
				activeId = space.id,
				isLoading = isLoading.value,
				spaces = spaces.value,
				spaceThumbnails = spaceThumbnails.value,
				onSelect = {
					onSwitchSpace(it)
					sheetOpen = false
				},
				onCreate = {
					viewModel.acceptIntent(SpaceSwitcherViewModel.SpaceSwitcherIntent.ResetSpaces)
					onCreateSpace(space)
				},
				onJoin = {},
				onDismiss = { sheetOpen = false },
			)
		}
	}

}