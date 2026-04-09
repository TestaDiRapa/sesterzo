package org.testadirapa.sesterzo.components.input

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalClipboard
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.testadirapa.sesterzo.styles.colors.SesterzoColors
import org.testadirapa.sesterzo.utils.newClipEntry
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.content_copy_icon

@Composable
fun Bip39RecoveryField(
	words: List<String>,
	title: String,
	label: String,
) {
	val scope = rememberCoroutineScope()
	val clipboardManager = LocalClipboard.current
	val columnsCount = 3
	val rows = words.chunked(columnsCount)

	Column {
		Row(
			modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
			verticalAlignment = Alignment.CenterVertically,
		) {
			Box(modifier = Modifier.weight(1f)) {
				Text(
					text = title,
					style = MaterialTheme.typography.titleMedium,
					color = colorScheme.onSurfaceVariant,
				)
			}
			IconButton(onClick = {
				scope.launch {
					clipboardManager.setClipEntry(newClipEntry(label, words.joinToString(" ")))
				}
			}) {
				Icon(
					painter = painterResource(Res.drawable.content_copy_icon),
					contentDescription = "Copy",
					tint = colorScheme.onSurfaceVariant,
				)
			}
		}
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.border(
					width = 1.dp,
					color = SesterzoColors.Gray100,
					shape = RoundedCornerShape(8.dp),
				)
				.padding(12.dp),
			verticalArrangement = Arrangement.spacedBy(8.dp),
		) {
			rows.forEachIndexed { rowIndex, rowWords ->
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.spacedBy(8.dp),
				) {
					rowWords.forEachIndexed { colIndex, word ->
						val wordIndex = rowIndex * columnsCount + colIndex + 1
						Row(
							modifier = Modifier.weight(1f),
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.spacedBy(6.dp),
						) {
							Text(
								text = wordIndex.toString(),
								style = MaterialTheme.typography.bodySmall,
								color = colorScheme.onSurfaceVariant,
								modifier = Modifier.widthIn(min = 20.dp),
							)
							Text(
								text = word,
								style = MaterialTheme.typography.bodyMedium,
								fontWeight = FontWeight.Medium,
								color = colorScheme.onSurface,
							)
						}
					}
				}
			}
		}
	}
}
