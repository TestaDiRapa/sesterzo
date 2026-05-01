package org.testadirapa.sesterzo.components.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.styles.colors.SpaceColor
import org.testadirapa.sesterzo.styles.colors.onSpaceColor
import org.testadirapa.sesterzo.utils.toImageBitmap

@Composable
fun SpaceThumbnailBadge(
	placeholderLetter: String,
	color: SpaceColor,
	imageBytes: Base64String?
) {
	val imageBitmap = imageBytes?.toImageBitmap()
	SpaceThumbnail(
		placeholderLetter = placeholderLetter,
		imageBitmap = imageBitmap,
		boxSize = 24.dp,
		fontSize = 11.sp,
		background = color
	)
}

@Composable
fun SpaceThumbnailSelector(
	placeholderLetter: String,
	imageBitmap: ImageBitmap?
) {
	SpaceThumbnail(
		placeholderLetter = placeholderLetter,
		imageBitmap = imageBitmap,
		boxSize = 144.dp,
		fontSize = 66.sp,
		background = SpaceColor.Amber
	)
}

@Composable
private fun SpaceThumbnail(
	placeholderLetter: String,
	imageBitmap: ImageBitmap?,
	boxSize: Dp,
	fontSize: TextUnit,
	background: SpaceColor
) {
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.size(boxSize)
			.clip(CircleShape)
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
					.background(background.color),
			) {
				Text(
					text = placeholderLetter,
					color = onSpaceColor,
					fontSize = fontSize,
					fontWeight = FontWeight.Bold,
				)
			}
		}
	}
}