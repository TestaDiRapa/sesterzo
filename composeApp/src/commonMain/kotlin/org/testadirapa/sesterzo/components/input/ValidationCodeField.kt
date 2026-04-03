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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.models.FormValue
import org.testadirapa.sesterzo.styles.colors.components.outlinedTextFieldColors
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.validation_code_field
import sesterzo.composeapp.generated.resources.validation_code_field_invalid
import sesterzo.composeapp.generated.resources.validation_code_field_placeholder

@Composable
fun ValidationCodeField(
	value: FormValue<String>,
	onValueChange: (String) -> Unit,
) {
	val focusManager = LocalFocusManager.current
	Column {
		Box(
			modifier = Modifier.padding(bottom = 4.dp),
		) {
			Text(
				text = stringResource(Res.string.validation_code_field),
				style = MaterialTheme.typography.titleMedium,
				color = colorScheme.onSurfaceVariant,
			)
		}
		OutlinedTextField(
			value = value.value ?: "",
			onValueChange = onValueChange,
			placeholder = { Text(stringResource(Res.string.validation_code_field_placeholder)) },
			isError = value.displayError,
			singleLine = true,
			keyboardOptions = KeyboardOptions(
				keyboardType = KeyboardType.NumberPassword,
				imeAction = ImeAction.Next
			),
			keyboardActions = KeyboardActions(
				onNext = { focusManager.moveFocus(FocusDirection.Down) }
			),
			supportingText = {
				if (value.displayError) {
					Text(stringResource(Res.string.validation_code_field_invalid), color = colorScheme.error)
				}
			},
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(8.dp),
			colors = outlinedTextFieldColors()
		)
	}
}