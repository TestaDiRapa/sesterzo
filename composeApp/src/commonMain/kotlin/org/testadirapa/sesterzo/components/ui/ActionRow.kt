package org.testadirapa.sesterzo.components.space

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.arrow_right

@Composable
fun ActionRow(
	leading: @Composable () -> Unit,
	title: String,
	subtitle: String?,
	titleColor: Color = MaterialTheme.colorScheme.onSurface,
	onClick: () -> Unit,
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.clickable(onClick = onClick)
			.padding(horizontal = 24.dp, vertical = 12.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		leading()
		Spacer(Modifier.width(12.dp))
		Column(Modifier.weight(1f)) {
			Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = titleColor)
			if (subtitle != null) {
				Text(
					subtitle,
					fontSize = 11.5.sp,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					modifier = Modifier.padding(top = 2.dp),
				)
			}
		}
		Icon(
			painter = painterResource(Res.drawable.arrow_right),
			contentDescription = "Do action",
			tint = MaterialTheme.colorScheme.onSurfaceVariant
		)
	}
}