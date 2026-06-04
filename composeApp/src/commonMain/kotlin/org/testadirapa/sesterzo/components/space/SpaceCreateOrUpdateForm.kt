package org.testadirapa.sesterzo.components.mobile.space

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.input.FormButton
import org.testadirapa.sesterzo.components.input.ImagePicker
import org.testadirapa.sesterzo.components.input.TextField
import org.testadirapa.sesterzo.models.FormValue
import org.testadirapa.sesterzo.models.Optional
import org.testadirapa.sesterzo.styles.colors.SpaceColor
import org.testadirapa.sesterzo.validators.defaultNameValidator
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.create_space_space_name
import sesterzo.composeapp.generated.resources.create_space_space_name_error
import sesterzo.composeapp.generated.resources.create_space_space_name_placeholder

@Composable
fun SpaceCreateOrUpdateForm(
	isLoading: Boolean,
	buttonLabel: String,
	onSubmit: (name: String, imageBytes: ByteArray?, color: SpaceColor) -> Unit,
	onError: (e: Throwable) -> Unit,
	name: String? = null,
	color: SpaceColor = SpaceColor.Amber,
	imageBytes: ByteArray? = null,
) {
	var imageBytes by remember { mutableStateOf(imageBytes) }
	var spaceName by remember {
		mutableStateOf(
			FormValue(
				value = name?.let { Optional.Present(it) } ?: Optional.Absent,
				validator = defaultNameValidator
			)
		)
	}
	var selectedColor by remember { mutableStateOf(color) }
	Column(
		verticalArrangement = Arrangement.spacedBy(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		ImagePicker(
			imageBytes = imageBytes,
			placeholderLetter = spaceName.value.orNull?.firstOrNull()?.uppercase() ?: "S",
			placeholderBackground = selectedColor,
			onImageSelected = { imageBytes = it },
			onError = {
				onError(IllegalStateException(it))
			},
		)
		SpaceColorPicker(
			selectedColor = selectedColor,
			onColorSelected = { selectedColor = it },
		)
		TextField(
			title = stringResource(Res.string.create_space_space_name),
			value = spaceName,
			onValueChange = {
				spaceName = spaceName.update(it)
			},
			placeholder = stringResource(Res.string.create_space_space_name_placeholder),
			errorMessage = stringResource(Res.string.create_space_space_name_error),
		)
		FormButton(
			text = buttonLabel,
			enabled = spaceName.isValid,
			isLoading = isLoading,
			onClick = {
				onSubmit(spaceName.validValue, imageBytes, selectedColor)
			}
		)
	}
}

@Composable
private fun SpaceColorPicker(
	selectedColor: SpaceColor,
	onColorSelected: (SpaceColor) -> Unit,
) {
	Column(
		modifier = Modifier.width(222.dp)
	) {
		SpaceColor.entries.chunked(5).forEach { colors ->
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceEvenly,
				verticalAlignment = Alignment.CenterVertically,
			) {
				colors.forEach { spaceColor ->
					val isSelected = spaceColor == selectedColor
					Box(
						modifier = Modifier
							.size(38.dp)
							.clip(RoundedCornerShape(12.dp))
							.background(
								if (isSelected) Color.Black.copy(alpha = 0.15f) else Color.Transparent
							)
							.clickable { onColorSelected(spaceColor) },
						contentAlignment = Alignment.Center,
					) {
						Box(
							modifier = Modifier
								.size(if (isSelected) 30.dp else 34.dp)
								.clip(RoundedCornerShape(12.dp))
								.background(spaceColor.color)
						)
					}
				}
			}
		}
	}

}