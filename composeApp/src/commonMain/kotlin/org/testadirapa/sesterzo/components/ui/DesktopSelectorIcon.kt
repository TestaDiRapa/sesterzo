package org.testadirapa.sesterzo.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RowScope.DesktopSelectorIcon(
	label: String,
	painter: Painter,
	color: Color,
) {
	Spacer(Modifier.width(4.dp))
	Box(
		Modifier
			.size(48.dp)
			.clip(RoundedCornerShape(12.dp))
			.background(colorScheme.surfaceContainerHigh),
		contentAlignment = Alignment.Center,
	) {
		Icon(
			modifier = Modifier.size(24.dp),
			painter = painter,
			contentDescription = null,
			tint = color,
		)
	}
	Spacer(Modifier.width(16.dp))
	Text(
		text = label,
		color = colorScheme.onSurfaceVariant,
		fontSize = 17.sp,
		fontWeight = FontWeight.Normal,
	)
}