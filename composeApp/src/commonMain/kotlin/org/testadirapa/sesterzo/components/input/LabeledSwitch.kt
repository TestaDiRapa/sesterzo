package org.testadirapa.sesterzo.components.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LabeledSwitch(
	label: String,
	initialValue: Boolean = false,
	onCheckedChange: (Boolean) -> Unit = {},
) {
	var checked by remember { mutableStateOf(initialValue) }
	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically,
	) {
		Text(
			text = label,
			style = MaterialTheme.typography.bodyMedium,
			color = colorScheme.onSurfaceVariant,
		)
		Switch(
			checked = checked,
			onCheckedChange = {
				checked = it
				onCheckedChange(it)
			},
		)
	}
}