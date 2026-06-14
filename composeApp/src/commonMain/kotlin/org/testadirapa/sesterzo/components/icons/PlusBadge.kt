package org.testadirapa.sesterzo.components.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import sesterzo.composeapp.generated.resources.plus

@Composable
fun PlusBadge() {
	Box(
		Modifier
			.size(36.dp)
			.clip(RoundedCornerShape(11.dp))
			.background(colorScheme.primaryContainer),
		contentAlignment = Alignment.Center,
	) {
		Icon(
			modifier = Modifier.height(24.dp).width(24.dp),
			painter = painterResource(Res.drawable.plus),
			contentDescription = null,
			tint = colorScheme.primary,
		)
	}
}