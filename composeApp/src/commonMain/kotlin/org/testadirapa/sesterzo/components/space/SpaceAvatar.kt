package org.testadirapa.sesterzo.components.space

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.styles.colors.colorOrDefault
import org.testadirapa.sesterzo.styles.colors.onSpaceColor
import org.testadirapa.sesterzo.utils.toImageBitmap

@Composable
fun SpaceAvatar(
	space: Space,
	thumbnail: Base64String?,
	size: Dp
) {
	val imageBitmap = thumbnail?.toImageBitmap()
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.size(size)
			.clip(RoundedCornerShape(11.dp))
	) {
		if (imageBitmap != null) {
			Image(
				painter = BitmapPainter(imageBitmap),
				contentDescription = "Selected image",
				contentScale = ContentScale.Crop,
				modifier = Modifier.matchParentSize(),
			)
		} else {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier
					.matchParentSize()
					.background(space.colorOrDefault().color),
			) {
				Text(
					text = space.name.first().uppercase(),
					color = onSpaceColor,
					fontWeight = FontWeight.Bold,
					fontSize = (size.value * 0.42f).sp,
				)
			}
		}
	}
}