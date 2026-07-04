package org.testadirapa.sesterzo.screens.main.mobile.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.icure.kryptom.utils.base64Decode
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.desktop.settings.SettingsRow
import org.testadirapa.sesterzo.components.desktop.settings.SettingsSection
import org.testadirapa.sesterzo.components.settings.KeyOverlay
import org.testadirapa.sesterzo.components.settings.OverlayContentType
import org.testadirapa.sesterzo.components.settings.UserSettingsSection
import org.testadirapa.sesterzo.components.space.SpaceCreateOrUpdateForm
import org.testadirapa.sesterzo.components.user.UserCurrencyUpdateForm
import org.testadirapa.sesterzo.components.user.UserNameUpdateForm
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.styles.colors.colorOrDefault
import org.testadirapa.sesterzo.viewmodel.components.SettingsPageViewModel
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.settings_page_confirm_update
import sesterzo.composeapp.generated.resources.settings_page_edit_space
import sesterzo.composeapp.generated.resources.settings_page_edit_space_subtitle
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
	val viewModel = viewModel(key = space.id) {
		SettingsPageViewModel(errorHandler = onError)
	}

	val isLoading = viewModel.loadingState.collectAsState()
	val currentUser = viewModel.currentUserState.collectAsState()

	var showSpaceUpdateSheet by remember { mutableStateOf(false) }
	val spaceUpdateSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	var showNameUpdateSheet by remember { mutableStateOf(false) }
	val nameUpdateSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	var showCurrencyUpdateSheet by remember { mutableStateOf(false) }
	val currencyUpdateSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	var overlayContentType by remember { mutableStateOf<OverlayContentType?>(null) }

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
		currentUser.value?.let { user ->
			UserSettingsSection(
				title = stringResource(Res.string.settings_page_user_settings),
				user = user,
				onEditName = { showNameUpdateSheet = true },
				onEditCurrency = { showCurrencyUpdateSheet = true },
				onChooseOverlay = { overlayContentType = it }
			)

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
							isLoading = isLoading.value,
							onSubmit = { currency ->
								viewModel.acceptIntent(SettingsPageViewModel.SettingsIntents.SetCurrency(currency))
								showCurrencyUpdateSheet = false
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
							isLoading = isLoading.value,
							onSubmit = { name ->
								viewModel.acceptIntent(SettingsPageViewModel.SettingsIntents.SetUserName(name))
								showNameUpdateSheet = false
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
					isLoading = isLoading.value,
					buttonLabel = stringResource(Res.string.settings_page_confirm_update),
					onError = onError,
					name = space.name,
					color = space.colorOrDefault(),
					imageBytes = spaceThumbnail?.let { base64Decode(it) },
					onSubmit = { name, image, color ->
						viewModel.acceptIntent(
							SettingsPageViewModel.SettingsIntents.UpdateSpace(
								spaceId = space.id,
								name = name,
								image = image,
								color = color.rgbColor,
								onSpaceUpdate = onSpaceUpdate,
							)
						)
						showSpaceUpdateSheet = false
					}
				)
			}
		}
	}

	overlayContentType?.also { type ->
		KeyOverlay(
			contentType = type,
			onDismiss = { overlayContentType = null },
			onError = onError,
		)
	}

}