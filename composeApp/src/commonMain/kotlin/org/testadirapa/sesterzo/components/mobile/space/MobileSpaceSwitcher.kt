package org.testadirapa.sesterzo.components.mobile.space

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.BuildKonfig
import org.testadirapa.sesterzo.components.loading.PulsingRoundedSquare
import org.testadirapa.sesterzo.components.ui.ActionRow
import org.testadirapa.sesterzo.components.space.SpaceRow
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.model.Timestamp
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.create_new_space
import sesterzo.composeapp.generated.resources.create_new_space_subtitle
import sesterzo.composeapp.generated.resources.join
import sesterzo.composeapp.generated.resources.join_space
import sesterzo.composeapp.generated.resources.join_space_subtitle
import sesterzo.composeapp.generated.resources.out_of
import sesterzo.composeapp.generated.resources.plus
import sesterzo.composeapp.generated.resources.swich_space_label
import sesterzo.composeapp.generated.resources.your_spaces

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobileSpaceSwitcher(
	activeId: String,
	openKey: Timestamp,
	onSelect: (Space) -> Unit,
	onCreate: () -> Unit,
	onJoin: () -> Unit,
	onDismiss: () -> Unit,
	onError: (Throwable) -> Unit
) {
	var isLoading by remember { mutableStateOf(false) }
	val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	var spaces by remember { mutableStateOf<List<Space>>(emptyList()) }

	LaunchedEffect(openKey) {
		isLoading = true
		runCatching {
			spaces = AppCtx.api.space.getSpaces()
			isLoading = false
		}.onFailure {
			onError(it)
		}
	}

	ModalBottomSheet(
		onDismissRequest = onDismiss,
		sheetState = sheetState,
		containerColor = MaterialTheme.colorScheme.surface,
		dragHandle = { BottomSheetDefaults.DragHandle() },
		shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
	) {
		Column(Modifier.padding(bottom = 28.dp)) {
			SheetHeader(count = spaces.size)

			if (!isLoading && !sheetState.isAnimationRunning) {
				Column(Modifier.padding(horizontal = 12.dp)) {
					spaces.forEach { space ->
						SpaceRow(
							space = space,
							active = space.id == activeId,
							onClick = { onSelect(space) },
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
						PulsingRoundedSquare(
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

@Composable
private fun SheetHeader(count: Int) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 14.dp),
		verticalAlignment = Alignment.Bottom,
	) {
		Column(Modifier.weight(1f)) {
			Text(
				text = stringResource(Res.string.swich_space_label),
				style = MaterialTheme.typography.labelSmall,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
			)
			Spacer(Modifier.height(4.dp))
			Text(
				text = stringResource(Res.string.your_spaces),
				fontSize = 17.sp,
				fontWeight = FontWeight.SemiBold,
				color = MaterialTheme.colorScheme.onSurface,
			)
		}
		Text(
			text = "$count ${stringResource(Res.string.out_of)} ${BuildKonfig.spaceLimit}",
			style = MaterialTheme.typography.labelMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
		)
	}
}

@Composable
private fun PlusBadge() {
	Box(
		Modifier
			.size(36.dp)
			.clip(RoundedCornerShape(11.dp))
			.background(MaterialTheme.colorScheme.primaryContainer),
		contentAlignment = Alignment.Center,
	) {
		Icon(
			modifier = Modifier.height(24.dp).width(24.dp),
			painter = painterResource(Res.drawable.plus),
			contentDescription = null,
			tint = MaterialTheme.colorScheme.primary,
		)
	}
}

@Composable
private fun JoinBadge() {
	Box(
		Modifier
			.size(36.dp)
			.clip(RoundedCornerShape(11.dp))
			.background(MaterialTheme.colorScheme.surfaceContainerHigh),
		contentAlignment = Alignment.Center,
	) {
		Icon(
			modifier = Modifier.height(24.dp).width(24.dp),
			painter = painterResource(Res.drawable.join),
			contentDescription = null,
			tint = MaterialTheme.colorScheme.onSurfaceVariant,
		)
	}
}