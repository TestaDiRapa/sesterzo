package org.testadirapa.sesterzo.components.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
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
	colors: ButtonColors = ButtonColors(
		containerColor = colorScheme.primary,
		contentColor = colorScheme.onPrimary,
		disabledContainerColor = colorScheme.surfaceContainerHigh,
		disabledContentColor = colorScheme.onTertiary,
	)
) {
	Button(
		onClick = onClick,
		enabled = enabled && !isLoading,
		modifier = Modifier.fillMaxWidth().height(48.dp),
		shape = RoundedCornerShape(8.dp),
		colors = colors
	) {
		if (isLoading) {
			CircularProgressIndicator(
				modifier = Modifier.size(20.dp),
				color = colorScheme.onSurfaceVariant,
				strokeWidth = 2.dp,
			)
		} else {
			Text(text)
		}
	}
}