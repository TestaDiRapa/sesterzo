package org.testadirapa.sesterzo.components.legal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.legal.LegalDocument
import org.testadirapa.sesterzo.legal.LegalDocuments
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.legal_github
import sesterzo.composeapp.generated.resources.legal_privacy_policy
import sesterzo.composeapp.generated.resources.legal_terms_of_service

@Composable
fun LegalFooter(
	modifier: Modifier = Modifier,
) {
	var shownDocument by remember { mutableStateOf<LegalDocument?>(null) }
	val uriHandler = LocalUriHandler.current
	Row(
		modifier = modifier.fillMaxWidth().padding(vertical = 12.dp),
		horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
		verticalAlignment = Alignment.CenterVertically,
	) {
		FooterLink(
			text = stringResource(Res.string.legal_privacy_policy),
			onClick = { shownDocument = LegalDocuments.PrivacyPolicy },
		)
		FooterSeparator()
		FooterLink(
			text = stringResource(Res.string.legal_terms_of_service),
			onClick = { shownDocument = LegalDocuments.TermsOfService },
		)
		FooterSeparator()
		FooterLink(
			text = stringResource(Res.string.legal_github),
			onClick = { uriHandler.openUri(LegalDocuments.GITHUB_URL) },
		)
	}
	shownDocument?.also { document ->
		LegalDocumentOverlay(
			document = document,
			onDismiss = { shownDocument = null },
		)
	}
}

@Composable
private fun FooterLink(
	text: String,
	onClick: () -> Unit,
) {
	Text(
		text = text,
		style = MaterialTheme.typography.bodySmall,
		color = colorScheme.onSurfaceVariant,
		modifier = Modifier.clickable(onClick = onClick),
	)
}

@Composable
private fun FooterSeparator() {
	Text(
		text = "•",
		style = MaterialTheme.typography.bodySmall,
		color = colorScheme.onSurfaceVariant,
	)
}