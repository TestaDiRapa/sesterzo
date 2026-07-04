package org.testadirapa.sesterzo.screens.main.desktop.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.icure.kryptom.utils.base64Decode
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.desktop.settings.SettingsRow
import org.testadirapa.sesterzo.components.desktop.settings.SettingsSection
import org.testadirapa.sesterzo.components.desktop.settings.SettingsSectionSelector
import org.testadirapa.sesterzo.components.scaffold.DesktopTopBarScaffold
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
import sesterzo.composeapp.generated.resources.bank
import sesterzo.composeapp.generated.resources.person
import sesterzo.composeapp.generated.resources.settings_page_confirm_update
import sesterzo.composeapp.generated.resources.settings_page_desktop_title
import sesterzo.composeapp.generated.resources.settings_page_edit_space
import sesterzo.composeapp.generated.resources.settings_page_edit_space_subtitle
import sesterzo.composeapp.generated.resources.settings_page_space_settings
import sesterzo.composeapp.generated.resources.settings_page_user_settings

private enum class SettingsPage { Space, User }

@Composable
fun DesktopSettingsScreen(
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
	var currentPage by remember { mutableStateOf<SettingsPage?>(null) }

	var showSpaceUpdateModal by remember { mutableStateOf(false) }
	var showNameUpdateModal by remember { mutableStateOf(false) }
	var showCurrencyUpdateModal by remember { mutableStateOf(false) }
	var overlayContentType by remember { mutableStateOf<OverlayContentType?>(null) }

	DesktopTopBarScaffold(
		headerComponent = {
			SettingsTopBar()
		}
	) {
		Row(
			modifier = Modifier.fillMaxSize(),
		) {
			Column(
				modifier = Modifier.weight(1f).padding(all = 16.dp)
			) {
				SettingsSectionSelector(
					label = stringResource(Res.string.settings_page_space_settings),
					isSelected = currentPage == SettingsPage.Space,
					painter = painterResource(Res.drawable.bank),
					color = colorScheme.primary,
					onClick = { currentPage = SettingsPage.Space }
				)
				Spacer(modifier = Modifier.height(16.dp))
				SettingsSectionSelector(
					label = stringResource(Res.string.settings_page_user_settings),
					isSelected = currentPage == SettingsPage.User,
					painter = painterResource(Res.drawable.person),
					color = colorScheme.primary,
					onClick = { currentPage = SettingsPage.User }
				)
			}
			VerticalDivider()
			Column(
				modifier = Modifier.weight(2f).fillMaxHeight().padding(all = 16.dp),
			) {
				when (currentPage) {
					SettingsPage.Space -> {
						SettingsSection {
							SettingsRow(
								label = stringResource(Res.string.settings_page_edit_space),
								subtitle = stringResource(Res.string.settings_page_edit_space_subtitle),
								onClick = { showSpaceUpdateModal = true }
							)
						}
					}
					SettingsPage.User -> {
						currentUser.value?.let { user ->
							UserSettingsSection(
								user = user,
								onEditName = { showNameUpdateModal = true },
								onEditCurrency = { showCurrencyUpdateModal = true },
								onChooseOverlay = { overlayContentType = it }
							)

							if (showCurrencyUpdateModal) {
								Dialog(onDismissRequest = { showCurrencyUpdateModal = false }) {
									Surface(
										shape = RoundedCornerShape(16.dp),
										color = colorScheme.surface,
										modifier = Modifier.width(480.dp)
									) {
										Column(
											modifier = Modifier.padding(all = 32.dp),
										) {
											UserCurrencyUpdateForm(
												user = user,
												buttonLabel = stringResource(Res.string.settings_page_confirm_update),
												isLoading = isLoading.value,
												onSubmit = { currency ->
													viewModel.acceptIntent(SettingsPageViewModel.SettingsIntents.SetCurrency(currency))
													showCurrencyUpdateModal = false
												}
											)
										}
									}
								}
							}
							if (showNameUpdateModal) {
								Dialog(onDismissRequest = { showNameUpdateModal = false }) {
									Surface(
										shape = RoundedCornerShape(16.dp),
										color = colorScheme.surface,
										modifier = Modifier.width(480.dp)
									) {
										Column(
											modifier = Modifier.padding(all = 32.dp),
										) {
											UserNameUpdateForm(
												user = user,
												buttonLabel = stringResource(Res.string.settings_page_confirm_update),
												isLoading = isLoading.value,
												onSubmit = { name ->
													viewModel.acceptIntent(
														SettingsPageViewModel.SettingsIntents.SetUserName(name)
													)
													showNameUpdateModal = false
												}
											)
										}
									}
								}
							}
						}
					}
					null -> {}
				}
			}
		}

		if (showSpaceUpdateModal) {
			Dialog(onDismissRequest = { showSpaceUpdateModal = false }) {
				Surface(
					shape = RoundedCornerShape(16.dp),
					color = colorScheme.surface,
					modifier = Modifier.width(480.dp)
				) {
					Column(
						modifier = Modifier.padding(all = 32.dp),
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
								showSpaceUpdateModal = false
							}
						)
					}
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
}

@Composable
fun SettingsTopBar() {
	Row(
		modifier = Modifier.padding(16.dp).fillMaxWidth().height(60.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Text(
			text = stringResource(Res.string.settings_page_desktop_title),
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.Bold,
			color = colorScheme.onBackground,
		)
	}
}