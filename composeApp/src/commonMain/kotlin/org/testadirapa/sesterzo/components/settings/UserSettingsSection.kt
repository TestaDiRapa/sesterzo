package org.testadirapa.sesterzo.components.settings

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.desktop.settings.SettingsRow
import org.testadirapa.sesterzo.components.desktop.settings.SettingsSection
import org.testadirapa.sesterzo.model.User
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.settings_page_current
import sesterzo.composeapp.generated.resources.settings_page_disable_report
import sesterzo.composeapp.generated.resources.settings_page_disable_report_subtitle
import sesterzo.composeapp.generated.resources.settings_page_display_private_key
import sesterzo.composeapp.generated.resources.settings_page_edit_currency
import sesterzo.composeapp.generated.resources.settings_page_edit_user_name
import sesterzo.composeapp.generated.resources.settings_page_enable_report
import sesterzo.composeapp.generated.resources.settings_page_enable_report_subtitle
import sesterzo.composeapp.generated.resources.settings_page_recovery_private_key_subtitle
import sesterzo.composeapp.generated.resources.settings_page_recovery_qr
import sesterzo.composeapp.generated.resources.settings_page_recovery_qr_subtitle
import sesterzo.composeapp.generated.resources.settings_page_recovery_sentence
import sesterzo.composeapp.generated.resources.settings_page_recovery_sentence_subtitle

@Composable
fun UserSettingsSection(
	title: String? = null,
	user: User,
	onEditName: () -> Unit,
	onEditCurrency: () -> Unit,
	onChooseOverlay: (OverlayContentType) -> Unit,
	onErrorOptIn: () -> Unit,
) {
	SettingsSection(
		title = title
	) {
		SettingsRow(
			label = stringResource(Res.string.settings_page_edit_user_name),
			subtitle = "${stringResource(Res.string.settings_page_current)}: ${user.name}",
			onClick = onEditName
		)
		HorizontalDivider(color = colorScheme.outline)
		SettingsRow(
			label = stringResource(Res.string.settings_page_edit_currency),
			subtitle = "${stringResource(Res.string.settings_page_current)}: ${user.preferredCurrency.name}",
			onClick = onEditCurrency
		)
		HorizontalDivider(color = colorScheme.outline)
		SettingsRow(
			label = stringResource(Res.string.settings_page_recovery_qr),
			subtitle = stringResource(Res.string.settings_page_recovery_qr_subtitle),
			onClick = { onChooseOverlay(OverlayContentType.QR) }
		)
		HorizontalDivider(color = colorScheme.outline)
		SettingsRow(
			label = stringResource(Res.string.settings_page_recovery_sentence),
			subtitle = stringResource(Res.string.settings_page_recovery_sentence_subtitle),
			onClick = { onChooseOverlay(OverlayContentType.RecoveryKey) }
		)
		HorizontalDivider(color = colorScheme.outline)
		SettingsRow(
			label = stringResource(Res.string.settings_page_display_private_key),
			subtitle = stringResource(Res.string.settings_page_recovery_private_key_subtitle),
			onClick = { onChooseOverlay(OverlayContentType.PrivateKey) }
		)
		HorizontalDivider(color = colorScheme.outline)
		SettingsRow(
			label = if (user.sendLogs) {
					stringResource(Res.string.settings_page_disable_report)
				} else {
					stringResource(Res.string.settings_page_disable_report_subtitle)
				},
			subtitle = if (user.sendLogs) {
				stringResource(Res.string.settings_page_enable_report)
			} else {
				stringResource(Res.string.settings_page_enable_report_subtitle)
			},
			onClick = onErrorOptIn,
			showIcon = false
		)
	}
}