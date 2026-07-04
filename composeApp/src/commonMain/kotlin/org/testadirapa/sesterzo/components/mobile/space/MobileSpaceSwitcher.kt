package org.testadirapa.sesterzo.components.mobile.space

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.BuildKonfig
import org.testadirapa.sesterzo.components.icons.JoinBadge
import org.testadirapa.sesterzo.components.icons.PlusBadge
import org.testadirapa.sesterzo.components.loading.PulsatingRoundedSquare
import org.testadirapa.sesterzo.components.ui.ActionRow
import org.testadirapa.sesterzo.components.space.SpaceRow
import org.testadirapa.sesterzo.components.space.SpaceSwitcherHeader
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Space
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.create_new_space
import sesterzo.composeapp.generated.resources.create_new_space_subtitle
import sesterzo.composeapp.generated.resources.join_space
import sesterzo.composeapp.generated.resources.join_space_subtitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobileSpaceSwitcher(
	activeId: String,
	spaces: List<Space>,
	spaceThumbnails: Map<String, Base64String>,
	isLoading: Boolean,
	onSelect: (Space, Base64String?) -> Unit,
	onCreate: () -> Unit,
	onJoin: () -> Unit,
	onDismiss: () -> Unit,
) {
	val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

	ModalBottomSheet(
		onDismissRequest = onDismiss,
		sheetState = sheetState,
		containerColor = MaterialTheme.colorScheme.surface,
		dragHandle = { BottomSheetDefaults.DragHandle() },
		shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
	) {
		Column(Modifier.padding(bottom = 28.dp)) {
			SpaceSwitcherHeader(
				count = spaces.size,
				modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 14.dp),
			)

			if (!isLoading && !sheetState.isAnimationRunning) {
				Column(Modifier.padding(horizontal = 12.dp)) {
					spaces.sortedBy { it.name }.forEach { space ->
						SpaceRow(
							space = space,
							picture = spaceThumbnails[space.id],
							active = space.id == activeId,
							onClick = { onSelect(space, spaceThumbnails[space.id]) },
						)
					}
				}

				if (spaces.size < BuildKonfig.spaceLimit) {
					HorizontalDivider(
						modifier = Modifier.padding(top = 10.dp),
						color = MaterialTheme.colorScheme.outlineVariant,
					)

					ActionRow(
						leading = { PlusBadge() },
						title = stringResource(Res.string.create_new_space),
						subtitle = stringResource(Res.string.create_new_space_subtitle),
						titleColor = MaterialTheme.colorScheme.primary,
						onClick = onCreate,
					)
					ActionRow(
						leading = { JoinBadge() },
						title = stringResource(Res.string.join_space),
						subtitle = stringResource(Res.string.join_space_subtitle),
						onClick = onJoin,
					)
				}
			} else {
				Column(Modifier.padding(horizontal = 12.dp)) {
					(0 until 4).forEach { idx ->
						PulsatingRoundedSquare(
							index = idx,
							total = 4,
							height = 50.dp,
							width = null
						)
						Spacer(Modifier.height(8.dp))
					}
				}
			}
		}
	}
}