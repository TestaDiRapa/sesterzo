package org.testadirapa.sesterzo.components.input

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.testadirapa.sesterzo.utils.toImageBitmap

@Composable
fun ImagePicker(
	imageBytes: ByteArray?,
	placeholderLetter: String,
	onImageSelected: (ByteArray) -> Unit,
	onError: (String) -> Unit = {},
	modifier: Modifier = Modifier,
) {
	val launch = rememberImagePickerLauncher(
		onImageSelected = onImageSelected,
		onError = onError,
	)
	val imageBitmap = remember(imageBytes) { imageBytes?.toImageBitmap() }

	Box(
		contentAlignment = Alignment.BottomEnd,
		modifier = modifier.size(144.dp),
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.size(144.dp)
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
						.background(MaterialTheme.colorScheme.tertiaryContainer),
				) {
					Text(
						text = placeholderLetter,
						color = MaterialTheme.colorScheme.onTertiaryContainer,
						fontSize = 66.sp,
						fontWeight = FontWeight.Bold,
					)
				}
			}
		}

		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.size(48.dp)
				.clip(CircleShape)
				.background(MaterialTheme.colorScheme.secondaryContainer)
				.clickable { launch() },
		) {
			Text(
				text = "+",
				color = MaterialTheme.colorScheme.onSecondaryContainer,
				fontSize = 16.sp,
				fontWeight = FontWeight.Bold,
			)
		}
	}
}