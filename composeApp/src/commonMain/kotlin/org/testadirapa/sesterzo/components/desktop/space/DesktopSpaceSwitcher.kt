package org.testadirapa.sesterzo.components.desktop.space

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.BuildKonfig
import org.testadirapa.sesterzo.components.icons.JoinBadge
import org.testadirapa.sesterzo.components.icons.PlusBadge
import org.testadirapa.sesterzo.components.loading.PulsatingRoundedSquare
import org.testadirapa.sesterzo.components.space.SpaceAvatar
import org.testadirapa.sesterzo.components.space.SpaceRow
import org.testadirapa.sesterzo.components.space.SpaceSwitcherHeader
import org.testadirapa.sesterzo.components.ui.ActionRow
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.viewmodel.components.SpaceSwitcherViewModel
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.arrow_down
import sesterzo.composeapp.generated.resources.create_new_space
import sesterzo.composeapp.generated.resources.create_new_space_subtitle
import sesterzo.composeapp.generated.resources.join_space
import sesterzo.composeapp.generated.resources.join_space_subtitle

private val MENU_WIDTH = 320.dp

@Composable
fun DesktopSpaceSwitcher(
	space: Space,
	spaceThumbnail: Base64String?,
	onSelect: (Space, Base64String?) -> Unit,
	onCreate: () -> Unit,
	onJoin: () -> Unit,
	onError: (Throwable) -> Unit
) {
	val viewModel = viewModel { SpaceSwitcherViewModel(onError) }
	val isLoading = viewModel.loadingState.collectAsState()
	val spaces = viewModel.spacesState.collectAsState()
	val spaceThumbnails = viewModel.spaceThumbnailsState.collectAsState()

	var expanded by remember { mutableStateOf(false) }
	val arrowRotation by animateFloatAsState(if (expanded) 180f else 0f)

	Box {
		Card(
			modifier = Modifier.clickable {
				expanded = true
				viewModel.acceptIntent(SpaceSwitcherViewModel.SpaceSwitcherIntent.RefreshSpaces)
			},
			border = BorderStroke(width = 1.dp, color = colorScheme.outline),
			colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant),
		) {
			Row(
				modifier = Modifier.fillMaxWidth().padding(8.dp),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy(16.dp)
				) {
					SpaceAvatar(
						space = space,
						thumbnail = spaceThumbnail,
						size = 36.dp
					)
					Text(
						text = space.name,
						fontSize = 14.5.sp,
						fontWeight = FontWeight.SemiBold,
						color = colorScheme.onSurface,
						maxLines = 1,
						overflow = TextOverflow.Ellipsis,
					)
				}
				Icon(
					modifier = Modifier.size(18.dp).rotate(arrowRotation),
					painter = painterResource(Res.drawable.arrow_down),
					contentDescription = "Show Space menu",
					tint = colorScheme.onSurfaceVariant,
				)
			}
		}

		DropdownMenu(
			expanded = expanded,
			onDismissRequest = { expanded = false },
			modifier = Modifier.width(MENU_WIDTH),
		) {
			SpaceSwitcherHeader(
				count = spaces.value.size,
				modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 6.dp, bottom = 12.dp)
			)

			if (isLoading.value) {
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
			} else {
				Column(Modifier.padding(horizontal = 12.dp)) {
					spaces.value.sortedBy { it.name }.forEach { item ->
						SpaceRow(
							space = item,
							picture = spaceThumbnails.value[item.id],
							active = item.id == space.id,
							onClick = {
								onSelect(item, spaceThumbnails.value[item.id])
								expanded = false
							},
						)
					}
				}

				if (spaces.value.size < BuildKonfig.spaceLimit) {
					HorizontalDivider(
						modifier = Modifier.padding(top = 10.dp),
						color = colorScheme.outlineVariant,
					)

					ActionRow(
						leading = { PlusBadge() },
						title = stringResource(Res.string.create_new_space),
						subtitle = stringResource(Res.string.create_new_space_subtitle),
						titleColor = colorScheme.primary,
						onClick = {
							viewModel.acceptIntent(SpaceSwitcherViewModel.SpaceSwitcherIntent.ResetSpaces)
							onCreate()
							expanded = false
						},
					)
					ActionRow(
						leading = { JoinBadge() },
						title = stringResource(Res.string.join_space),
						subtitle = stringResource(Res.string.join_space_subtitle),
						onClick = {
							onJoin()
							expanded = false
						},
					)
				}
			}
		}
	}
}