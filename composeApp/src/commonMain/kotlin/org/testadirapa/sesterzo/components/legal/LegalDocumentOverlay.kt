package org.testadirapa.sesterzo.components.legal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.legal.LegalDocument
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.legal_close

@Composable
fun LegalDocumentOverlay(
	document: LegalDocument,
	onDismiss: () -> Unit,
) {
	Dialog(onDismissRequest = onDismiss) {
		Card(
			modifier = Modifier
				.padding(vertical = 32.dp)
				.fillMaxWidth(),
			border = BorderStroke(width = 1.dp, color = colorScheme.outline),
			colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
		) {
			Column(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp),
			) {
				Text(
					text = document.title,
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.Bold,
					color = colorScheme.onSurface,
				)
				Text(
					text = document.lastUpdated,
					style = MaterialTheme.typography.bodySmall,
					color = colorScheme.onSurfaceVariant,
				)
			}
			Column(
				modifier = Modifier
					.weight(weight = 1f, fill = false)
					.heightIn(max = 480.dp)
					.padding(horizontal = 16.dp)
					.padding(top = 16.dp)
					.verticalScroll(rememberScrollState()),
				verticalArrangement = Arrangement.spacedBy(12.dp),
			) {
				document.sections.forEach { section ->
					Column {
						section.heading?.also { heading ->
							Text(
								text = heading,
								style = MaterialTheme.typography.titleMedium,
								fontWeight = FontWeight.SemiBold,
								color = colorScheme.onSurface,
							)
						}
						Text(
							text = section.body,
							style = MaterialTheme.typography.bodyMedium,
							color = colorScheme.onSurfaceVariant,
						)
					}
				}
			}
			TextButton(
				onClick = onDismiss,
				modifier = Modifier.align(Alignment.End).padding(8.dp),
			) {
				Text(stringResource(Res.string.legal_close))
			}
		}
	}
}
