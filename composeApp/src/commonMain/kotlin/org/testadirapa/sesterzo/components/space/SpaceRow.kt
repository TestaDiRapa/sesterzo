package org.testadirapa.sesterzo.components.space

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.model.Space
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.just_you
import sesterzo.composeapp.generated.resources.members

@Composable
fun SpaceRow(
	space: Space,
	active: Boolean,
	onClick: () -> Unit,
) {
	val activeBg = if (active) {
		MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
	} else {
		Color.Transparent
	}
	val activeBorder = if (active)
		MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
	else Color.Transparent

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 1.dp)
			.clip(RoundedCornerShape(12.dp))
			.background(activeBg)
			.border(1.dp, activeBorder, RoundedCornerShape(12.dp))
			.clickable(onClick = onClick)
			.padding(horizontal = 12.dp, vertical = 6.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		SpaceAvatar(space = space, size = 36.dp)

		Spacer(Modifier.width(12.dp))

		Column(Modifier.weight(1f)) {
			Row(verticalAlignment = Alignment.CenterVertically) {
				Text(
					text = space.name,
					fontSize = 14.5.sp,
					fontWeight = FontWeight.SemiBold,
					color = MaterialTheme.colorScheme.onSurface,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
				)
			}
			Row(verticalAlignment = Alignment.CenterVertically) {
				Text(
					text = if (space.users.size == 1) {
						stringResource(Res.string.just_you)
					} else {
						"${space.users.size} ${stringResource(Res.string.members)}"
					},
					fontSize = 11.5.sp,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
				)
			}
		}
	}
}