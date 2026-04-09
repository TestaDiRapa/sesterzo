package org.testadirapa.sesterzo.components.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FormButton(
	onClick: () -> Unit,
	text: String,
	enabled: Boolean = true,
	isLoading: Boolean = false,
) {
	Button(
		onClick = onClick,
		enabled = enabled && !isLoading,
		modifier = Modifier.fillMaxWidth().height(48.dp),
		shape = RoundedCornerShape(8.dp),
		colors = ButtonColors(
			containerColor = MaterialTheme.colorScheme.secondary,
			contentColor = MaterialTheme.colorScheme.onSecondary,
			disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
			disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
		)
	) {
		if (isLoading) {
			CircularProgressIndicator(
				modifier = Modifier.size(20.dp),
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				strokeWidth = 2.dp,
			)
		} else {
			Text(text)
		}
	}
}