package org.testadirapa.sesterzo.screens.main.mobile.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.icure.kryptom.utils.base64Decode
import com.icure.kryptom.utils.base64Encode
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.components.space.SpaceCreateOrUpdateForm
import org.testadirapa.sesterzo.components.text.MenuElementWithSubtitle
import org.testadirapa.sesterzo.components.user.UserCurrencyUpdateForm
import org.testadirapa.sesterzo.components.user.UserNameUpdateForm
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.model.User
import org.testadirapa.sesterzo.styles.colors.colorOrDefault
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.arrow_right
import sesterzo.composeapp.generated.resources.settings_page_confirm_update
import sesterzo.composeapp.generated.resources.settings_page_current
import sesterzo.composeapp.generated.resources.settings_page_edit_currency
import sesterzo.composeapp.generated.resources.settings_page_edit_space
import sesterzo.composeapp.generated.resources.settings_page_edit_space_subtitle
import sesterzo.composeapp.generated.resources.settings_page_edit_user_name
import sesterzo.composeapp.generated.resources.settings_page_space_settings
import sesterzo.composeapp.generated.resources.settings_page_user_settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMobilePage(
	space: Space,
	spaceThumbnail: Base64String?,
	onSpaceUpdate: (space: Space, thumbnail: Base64String?) -> Unit,
	onError: (e: Throwable) -> Unit
) {
	val scope = rememberCoroutineScope()
	var isLoading by remember { mutableStateOf(false) }
	var currentUser by remember { mutableStateOf<User?>(null) }
	var showSpaceUpdateSheet by remember { mutableStateOf(false) }
	val spaceUpdateSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	var showNameUpdateSheet by remember { mutableStateOf(false) }
	val nameUpdateSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	var showCurrencyUpdateSheet by remember { mutableStateOf(false) }
	val currencyUpdateSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

	LaunchedEffect(Unit) {
		runCatching { currentUser = AppCtx.api.user.getCurrentUser() }.onFailure(onError)
	}

	Column {
		SettingsSection(
			title = stringResource(Res.string.settings_page_space_settings)
		) {
			SettingsRow(
				label = stringResource(Res.string.settings_page_edit_space),
				subtitle = stringResource(Res.string.settings_page_edit_space_subtitle),
				onClick = { showSpaceUpdateSheet = true }
			)
		}
		Spacer(modifier = Modifier.height(16.dp))
		currentUser?.let { user ->
			SettingsSection(
				title = stringResource(Res.string.settings_page_user_settings)
			) {
				SettingsRow(
					label = stringResource(Res.string.settings_page_edit_user_name),
					subtitle = "${stringResource(Res.string.settings_page_current)}: ${user.name}",
					onClick = { showNameUpdateSheet = true }
				)
				HorizontalDivider(color = colorScheme.outline)
				SettingsRow(
					label = stringResource(Res.string.settings_page_edit_currency),
					subtitle = "${stringResource(Res.string.settings_page_current)}: ${user.preferredCurrency.name}",
					onClick = { showCurrencyUpdateSheet = true }
				)
			}

			if (showCurrencyUpdateSheet) {
				ModalBottomSheet(
					onDismissRequest = { showCurrencyUpdateSheet = false },
					sheetState = currencyUpdateSheetState,
					containerColor = colorScheme.surface,
				) {
					Column(
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
					) {
						UserCurrencyUpdateForm(
							user = user,
							buttonLabel = stringResource(Res.string.settings_page_confirm_update),
							isLoading = isLoading,
							onSubmit = { currency ->
								scope.launch {
									isLoading = true
									runCatching {
										currentUser = AppCtx.api.user.setCurrency(currency = currency)
										AppCtx.currency = currency
									}.onFailure(onError)
									isLoading = false
									showCurrencyUpdateSheet = false
								}
							}
						)
					}
				}
			}
			if (showNameUpdateSheet) {
				ModalBottomSheet(
					onDismissRequest = { showNameUpdateSheet = false },
					sheetState = nameUpdateSheetState,
					containerColor = colorScheme.surface,
				) {
					Column(
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
					) {
						UserNameUpdateForm(
							user = user,
							buttonLabel = stringResource(Res.string.settings_page_confirm_update),
							isLoading = isLoading,
							onSubmit = { name ->
								scope.launch {
									isLoading = true
									runCatching {
										currentUser = AppCtx.api.user.setName(name = name)
									}.onFailure(onError)
									isLoading = false
									showNameUpdateSheet = false
								}
							}
						)
					}
				}
			}
		}
	}

	if (showSpaceUpdateSheet) {
		ModalBottomSheet(
			onDismissRequest = { showSpaceUpdateSheet = false },
			sheetState = spaceUpdateSheetState,
			containerColor = colorScheme.surface,
		) {
			Column(
				modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
			) {
				SpaceCreateOrUpdateForm(
					isLoading = isLoading,
					buttonLabel = stringResource(Res.string.settings_page_confirm_update),
					onError = onError,
					name = space.name,
					color = space.colorOrDefault(),
					imageBytes = spaceThumbnail?.let { base64Decode(it) },
					onSubmit = { name, image, color ->
						scope.launch {
							isLoading = true
							runCatching {
								val result = AppCtx.api.space.updateSpace(
									spaceId = space.id,
									name = name,
									picture = image,
									color = color.rgbColor
								)
								onSpaceUpdate(result, image?.let { base64Encode(it) })
							}.onFailure(onError)
							isLoading = false
							showSpaceUpdateSheet = false
						}
					}
				)
			}
		}
	}

}

@Composable
private fun SettingsSection(
	title: String,
	content: @Composable ColumnScope.() -> Unit,
) {
	SectionTitle(title = title)
	Spacer(modifier = Modifier.height(8.dp))
	Card(
		modifier = Modifier.fillMaxWidth(),
		border = BorderStroke(width = 1.dp, color = colorScheme.outline),
		colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
		content = content
	)
}

@Composable
private fun SettingsRow(
	label: String,
	subtitle: String,
	onClick: () -> Unit,
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(12.dp)
			.clickable(onClick = onClick),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			Spacer(Modifier.width(4.dp))
			Box(
				modifier = Modifier
					.width(5.dp)
					.height(40.dp)
					.background(colorScheme.primary, RoundedCornerShape(5.dp))
			)
			Spacer(Modifier.width(16.dp))
			MenuElementWithSubtitle(
				label = label,
				subtitle = subtitle,
			)
		}
		Icon(
			modifier = Modifier.size(24.dp),
			painter = painterResource(Res.drawable.arrow_right),
			contentDescription = "Next",
			tint = colorScheme.onSurfaceVariant,
		)
	}
}

@Composable
private fun SectionTitle(
	title: String
) {
	Row(
		modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
	) {
		Text(
			text = title,
			style = MaterialTheme.typography.titleMedium,
			fontWeight = FontWeight.SemiBold,
			color = colorScheme.onBackground,
		)
	}
}