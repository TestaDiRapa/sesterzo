package org.testadirapa.sesterzo.components.input

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.testadirapa.sesterzo.models.FormValue
import org.testadirapa.sesterzo.styles.colors.components.outlinedTextFieldColors

@Composable
fun <T> TextField(
	value: FormValue<T>,
	title: String? = null,
	placeholder: String,
	errorMessage: String,
	enabled: Boolean = true,
	onValueChange: (String) -> Unit,
	modifier: Modifier = Modifier.fillMaxWidth(),
	multiline: Boolean = false,
	minLines: Int = 1,
	maxLines: Int = if (multiline) Int.MAX_VALUE else 1,
) {
	val focusManager = LocalFocusManager.current
	Column {
		if (title != null) {
			Box(
				modifier = Modifier.padding(bottom = 4.dp),
			) {
				Text(
					text = title,
					style = MaterialTheme.typography.titleMedium,
					color = colorScheme.onSurfaceVariant,
				)
			}
		}
		OutlinedTextField(
			value = value.value.orNull?.toString() ?: "",
			enabled = enabled,
			onValueChange = onValueChange,
			placeholder = { Text(placeholder) },
			isError = value.displayError,
			singleLine = !multiline,
			minLines = minLines,
			maxLines = maxLines,
			keyboardOptions = KeyboardOptions(
				imeAction = if (multiline) ImeAction.Default else ImeAction.Next
			),
			keyboardActions = KeyboardActions(
				onNext = if (!multiline) {
					{ focusManager.moveFocus(FocusDirection.Down) }
				} else null
			),
			supportingText = {
				if (value.displayError) {
					Text(errorMessage, color = colorScheme.error)
				}
			},
			modifier = modifier,
			shape = RoundedCornerShape(8.dp),
			colors = outlinedTextFieldColors()
		)
	}
}