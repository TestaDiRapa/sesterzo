package org.testadirapa.sesterzo.components.input

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.testadirapa.sesterzo.styles.colors.components.outlinedTextFieldColors
import org.testadirapa.sesterzo.utils.newClipEntry
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.content_copy_icon

@Composable
fun ReadOnlyCopyField(
	value: String,
	title: String,
	label: String,
) {
	val scope = rememberCoroutineScope()
	val clipboardManager = LocalClipboard.current
	Column {
		Box(modifier = Modifier.padding(bottom = 4.dp)) {
			Text(
				text = title,
				style = MaterialTheme.typography.titleMedium,
				color = colorScheme.onSurfaceVariant,
			)
		}
		OutlinedTextField(
			value = value,
			onValueChange = {},
			readOnly = true,
			singleLine = true,
			trailingIcon = {
				IconButton(onClick = {
					scope.launch {
						clipboardManager.setClipEntry(newClipEntry(label, value))
					}
				}) {
					Icon(
						painter = painterResource(Res.drawable.content_copy_icon),
						contentDescription = "Copy",
						tint = colorScheme.onSurfaceVariant,
					)
				}
			},
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(8.dp),
			colors = outlinedTextFieldColors(),
		)
	}
}
