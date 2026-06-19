package org.testadirapa.sesterzo.components.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.join

@Composable
fun JoinBadge() {
	Box(
		Modifier
			.size(36.dp)
			.clip(RoundedCornerShape(11.dp))
			.background(colorScheme.surfaceContainerHigh),
		contentAlignment = Alignment.Center,
	) {
		Icon(
			modifier = Modifier.size(24.dp),
			painter = painterResource(Res.drawable.join),
			contentDescription = null,
			tint = colorScheme.onSurfaceVariant,
		)
	}
}