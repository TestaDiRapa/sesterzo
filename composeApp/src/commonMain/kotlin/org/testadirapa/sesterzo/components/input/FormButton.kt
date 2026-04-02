package org.testadirapa.sesterzo.components.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FormButton(
	onClick: () -> Unit,
	enabled: Boolean,
	text: String
) {
	Button(
		onClick = onClick,
		enabled = enabled,
		modifier = Modifier.fillMaxWidth().height(48.dp),
		shape = RoundedCornerShape(8.dp),
		colors = ButtonColors(
			containerColor = MaterialTheme.colorScheme.secondary,
			contentColor = MaterialTheme.colorScheme.onSecondary,
			disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
			disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
		)
	) {
		Text(text)
	}
}