package org.testadirapa.sesterzo.components.settings

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalUriHandler
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.desktop.settings.SettingsRow
import org.testadirapa.sesterzo.components.desktop.settings.SettingsSection
import org.testadirapa.sesterzo.components.legal.LegalDocumentOverlay
import org.testadirapa.sesterzo.legal.LegalDocument
import org.testadirapa.sesterzo.legal.LegalDocuments
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.legal_github
import sesterzo.composeapp.generated.resources.legal_privacy_policy
import sesterzo.composeapp.generated.resources.legal_terms_of_service
import sesterzo.composeapp.generated.resources.settings_page_github_subtitle
import sesterzo.composeapp.generated.resources.settings_page_privacy_subtitle
import sesterzo.composeapp.generated.resources.settings_page_terms_subtitle

@Composable
fun AboutSection(
	title: String? = null,
	showEndIcon: Boolean = true,
) {
	var shownDocument by remember { mutableStateOf<LegalDocument?>(null) }
	val uriHandler = LocalUriHandler.current
	SettingsSection(
		title = title
	) {
		SettingsRow(
			label = stringResource(Res.string.legal_privacy_policy),
			subtitle = stringResource(Res.string.settings_page_privacy_subtitle),
			onClick = { shownDocument = LegalDocuments.PrivacyPolicy },
			showIcon = showEndIcon
		)
		HorizontalDivider(color = colorScheme.outline)
		SettingsRow(
			label = stringResource(Res.string.legal_terms_of_service),
			subtitle = stringResource(Res.string.settings_page_terms_subtitle),
			onClick = { shownDocument = LegalDocuments.TermsOfService },
			showIcon = showEndIcon
		)
		HorizontalDivider(color = colorScheme.outline)
		SettingsRow(
			label = stringResource(Res.string.legal_github),
			subtitle = stringResource(Res.string.settings_page_github_subtitle),
			onClick = { uriHandler.openUri(LegalDocuments.GITHUB_URL) },
			showIcon = showEndIcon
		)
	}
	shownDocument?.also { document ->
		LegalDocumentOverlay(
			document = document,
			onDismiss = { shownDocument = null },
		)
	}
}