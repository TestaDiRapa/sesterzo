package org.testadirapa.sesterzo.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.iconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.input.FormButton
import org.testadirapa.sesterzo.components.input.ImagePicker
import org.testadirapa.sesterzo.components.input.TextField
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.models.FormValue
import org.testadirapa.sesterzo.validators.NotBlankValidator
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.arrow_back
import sesterzo.composeapp.generated.resources.create_space_create_button
import sesterzo.composeapp.generated.resources.create_space_first_space
import sesterzo.composeapp.generated.resources.create_space_other_space
import sesterzo.composeapp.generated.resources.create_space_space_name
import sesterzo.composeapp.generated.resources.create_space_space_name_error
import sesterzo.composeapp.generated.resources.create_space_space_name_placeholder

@Composable
fun CreateSpaceScreen(
	currentSpace: Space?,
	isMobile: Boolean,
	isLoading: Boolean,
	onError: (throwable: Throwable) -> Unit,
	onCreateSpace: (name: String, image: ByteArray?) -> Unit,
	onCancel: (Space) -> Unit,
) {
	var imageBytes by remember { mutableStateOf<ByteArray?>(null) }
	var spaceName by remember { mutableStateOf(FormValue(validator = NotBlankValidator)) }
	Scaffold { innerPadding ->
		Box(
			modifier = Modifier.fillMaxSize().padding(innerPadding),
			contentAlignment = Alignment.Center,
		) {
			val content: @Composable () -> Unit = {
				Column(
					modifier = Modifier
						.fillMaxSize()
						.padding(horizontal = 24.dp)
						.padding(vertical = 24.dp),
				) {
					Row(
						verticalAlignment = Alignment.CenterVertically,
					) {
						if (currentSpace != null) {
							IconButton(
								onClick = { onCancel(currentSpace) },
								colors = iconButtonColors(
									containerColor = colorScheme.surfaceVariant,
									contentColor = colorScheme.onSurfaceVariant,
									disabledContainerColor = colorScheme.surfaceContainerHigh,
								),
								shape = RoundedCornerShape(8.dp)
							) {
								Icon(
									modifier = Modifier.width(24.dp).height(24.dp),
									painter = painterResource(Res.drawable.arrow_back),
									contentDescription = "Previous",
									tint = colorScheme.onSurfaceVariant,
								)
							}
						}
						Row(
							modifier = Modifier.fillMaxWidth(),
							horizontalArrangement = Arrangement.Center,
						) {
							Text(
								text = if (currentSpace == null) {
									stringResource(Res.string.create_space_first_space)
								} else {
									stringResource(Res.string.create_space_other_space)
								},
								style = MaterialTheme.typography.titleLarge,
								fontWeight = FontWeight.Bold,
								color = colorScheme.onBackground,
							)
						}
					}
					Box(
						modifier = Modifier.weight(1f).fillMaxWidth(),
						contentAlignment = Alignment.Center,
					) {
						Column(
							verticalArrangement = Arrangement.spacedBy(16.dp),
							horizontalAlignment = Alignment.CenterHorizontally,
						) {
							ImagePicker(
								imageBytes = imageBytes,
								placeholderLetter = "B",
								onImageSelected = { imageBytes = it },
								onError = {
									onError(IllegalStateException(it))
								},
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
								text = stringResource(Res.string.create_space_create_button),
								enabled = spaceName.isValid,
								isLoading = isLoading,
								onClick = {
									onCreateSpace(spaceName.validValue, imageBytes)
								}
							)
						}
					}
				}

			}

			if (isMobile) {
				content()
			} else {
				Card(
					modifier = Modifier
						.widthIn(max = 1200.dp)
						.heightIn(max = 720.dp),
					border = BorderStroke(width = 2.dp, color = colorScheme.onBackground),
					colors = CardDefaults.cardColors(containerColor = colorScheme.background),
				) {
					content()
				}
			}
		}
	}

}