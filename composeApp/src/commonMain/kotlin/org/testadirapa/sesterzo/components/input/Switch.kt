package org.testadirapa.sesterzo.components.input

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.testadirapa.sesterzo.styles.colors.SesterzoColors

private val TrackWidth = 38.dp
private val TrackHeight = 22.dp
private val ThumbSize = 18.dp
private val ThumbInset = 1.dp
private const val AnimMs = 180

@Composable
fun Switch(
	checked: Boolean,
	onCheckedChange: ((Boolean) -> Unit)?,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	onColor: Color = MaterialTheme.colorScheme.primary,
) {
	// Track
	val trackBg by animateColorAsState(
		targetValue = when {
			!enabled && checked -> onColor.copy(alpha = 0.35f)
			!enabled -> MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.6f)
			checked -> onColor
			else -> MaterialTheme.colorScheme.surfaceContainerHigh
		},
		animationSpec = tween(AnimMs),
		label = "switch-track",
	)
	val trackBorder by animateColorAsState(
		targetValue = when {
			!enabled -> MaterialTheme.colorScheme.outlineVariant
			checked -> onColor
			else -> MaterialTheme.colorScheme.outline
		},
		animationSpec = tween(AnimMs),
		label = "switch-border",
	)

	// Thumb
	val maxOffset = TrackWidth - ThumbSize - (ThumbInset * 2)
	val thumbX by animateDpAsState(
		targetValue = if (checked) maxOffset else 0.dp,
		animationSpec = tween(AnimMs),
		label = "switch-thumb-x",
	)
	val thumbColor by animateColorAsState(
		targetValue = when {
			!enabled && checked -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
			!enabled -> MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.6f)
			checked -> MaterialTheme.colorScheme.onPrimary
			else -> MaterialTheme.colorScheme.onSurfaceVariant
		},
		animationSpec = tween(AnimMs),
		label = "switch-thumb-color",
	)

	val toggleable = if (onCheckedChange != null) {
		Modifier.toggleable(
			value = checked,
			enabled = enabled,
			role = Role.Switch,
			interactionSource = remember { MutableInteractionSource() },
			indication = null,
			onValueChange = onCheckedChange,
		)
	} else Modifier

	Box(
		modifier = modifier
			.size(TrackWidth, TrackHeight)
			.clip(RoundedCornerShape(percent = 50))
			.background(color = trackBg)
			.border(1.dp, trackBorder, RoundedCornerShape(percent = 50))
			.then(toggleable)
			.padding(ThumbInset),
	) {
		Box(
			modifier = Modifier
				.size(ThumbSize)
				.offset(x = thumbX)
				.clip(CircleShape)
				.background(thumbColor),
		)
	}
}