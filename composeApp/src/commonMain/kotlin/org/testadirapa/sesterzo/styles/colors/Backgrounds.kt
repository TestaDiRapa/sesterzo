package org.testadirapa.sesterzo.styles.colors

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale

@Composable
fun Modifier.sesterzoGlow(): Modifier {
	val bg = colorScheme.background
	val amber = colorScheme.primary
	val mint = LocalFinanceColors.current.saved
	return this
		.background(bg)
		.drawBehind {
			radialGlow(
				color = amber.copy(alpha = 0.10f),
				centerX = 0.12f,
				centerY = 0.78f,
				radiusX = 1.20f,
				radiusY = 0.90f,
			)
			radialGlow(
				color = mint.copy(alpha = 0.05f),
				centerX = 0.95f,
				centerY = 0.00f,
				radiusX = 0.90f,
				radiusY = 0.70f,
			)
		}
}

private fun DrawScope.radialGlow(
	color: Color,
	centerX: Float, centerY: Float,
	radiusX: Float, radiusY: Float,
) {
	val cx = size.width * centerX
	val cy = size.height * centerY
	val rx = size.width * radiusX
	val ry = size.height * radiusY

	scale(scaleX = rx / ry, scaleY = 1f, pivot = Offset(cx, cy)) {
		drawRect(
			brush = Brush.radialGradient(
				colors = listOf(color, Color.Transparent),
				center = Offset(cx, cy),
				radius = ry,
			),
			size = size
		)
	}
}