package org.testadirapa.sesterzo.components.loading

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PulsingRoundedSquare(
	index: Int,
	total: Int,
	height: Dp,
	width: Dp?,
) {
	val animatable = remember { Animatable(initialValue = (index * (1f / total.toFloat())) % 1f) }

	LaunchedEffect(Unit) {
		animatable.animateTo(
			targetValue = 1f,
			animationSpec = tween(
				durationMillis = (animatable.value * 800).toInt(),
				easing = FastOutSlowInEasing
			)
		)
		// Then loop forever from 0
		animatable.animateTo(
			targetValue = 0f,
			animationSpec = tween(0) // instant reset
		)
		while (true) {
			animatable.animateTo(1f, animationSpec = tween(800, easing = FastOutSlowInEasing))
			animatable.animateTo(0f, animationSpec = tween(800, easing = FastOutSlowInEasing))
		}
	}

	val alpha = 0.2f + animatable.value * 0.4f

	Box(
		modifier = Modifier
			.height(height)
			.let {
				if (width != null) {
					it.width(width)
				} else {
					it.fillMaxWidth()
				}
			}
			.clip(RoundedCornerShape(12.dp))
			.background(Color.White.copy(alpha = alpha))
	)
}