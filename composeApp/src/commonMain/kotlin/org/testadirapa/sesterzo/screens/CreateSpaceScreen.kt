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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.mobile.space.SpaceCreateOrUpdateForm
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.styles.colors.SpaceColor
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.arrow_back
import sesterzo.composeapp.generated.resources.create_space_create_button
import sesterzo.composeapp.generated.resources.create_space_first_space
import sesterzo.composeapp.generated.resources.create_space_other_space

@Composable
fun CreateSpaceScreen(
	currentSpace: Space?,
	isMobile: Boolean,
	isLoading: Boolean,
	onError: (throwable: Throwable) -> Unit,
	onCreateSpace: (name: String, image: ByteArray?, color: SpaceColor) -> Unit,
	onCancel: (Space) -> Unit,
) {
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
						SpaceCreateOrUpdateForm(
							isLoading = isLoading,
							buttonLabel = stringResource(Res.string.create_space_create_button),
							onSubmit = onCreateSpace,
							onError = onError,
						)
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