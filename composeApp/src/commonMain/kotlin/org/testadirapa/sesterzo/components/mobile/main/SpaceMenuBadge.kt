package org.testadirapa.sesterzo.components.mobile.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.testadirapa.sesterzo.components.ui.SpaceThumbnailBadge
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.styles.colors.colorOrDefault
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.arrow_down

@Composable
fun SpaceMenuBadge(
	space: Space,
	onClick: () -> Unit,
) {
	Card(
		modifier = Modifier.clickable(onClick = onClick),
		border = BorderStroke(width = 1.dp, color = colorScheme.outline),
		colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
		shape = RoundedCornerShape(32.dp),
	) {
		Row(
			modifier = Modifier
				.height(35.dp)
				.padding(horizontal = 8.dp),
			verticalAlignment = Alignment.CenterVertically,
		) {
			SpaceThumbnailBadge(
				placeholderLetter = space.name.first().uppercase(),
				color = space.colorOrDefault(),
				imageBytes = space.picture
			)
			Spacer(modifier = Modifier.width(8.dp))
			Text(
				text = space.name,
				style = MaterialTheme.typography.bodyLarge,
				fontWeight = FontWeight.Bold,
				color = colorScheme.onSurface,
			)
			Spacer(modifier = Modifier.width(8.dp))
			Icon(
				modifier = Modifier.height(18.dp).width(18.dp),
				painter = painterResource(Res.drawable.arrow_down),
				contentDescription = "Show Space menu",
				tint = colorScheme.onSurfaceVariant,
			)
		}
	}
}