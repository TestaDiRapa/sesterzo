package org.testadirapa.sesterzo.components.input

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.models.FormValue
import org.testadirapa.sesterzo.styles.colors.components.outlinedTextFieldColors

@Composable
fun AmountField(
	value: FormValue<Amount>,
	enabled: Boolean = true,
	hasPlaceholder: Boolean = false,
	onValueChange: (Amount) -> Unit,
	textStyle: TextStyle,
	symbolOffset: Dp = 2.dp,
	modifier: Modifier = Modifier.fillMaxWidth(),
	backgroundColor: Color? = null,
) {
	val focusManager = LocalFocusManager.current
	Row(
		verticalAlignment = Alignment.CenterVertically,
	) {
		OutlinedTextField(
			value = AppCtx.currency.formWriter(value.value.orNull ?: 0),
			enabled = enabled,
			prefix = {
				Row {
					Text(
						text = AppCtx.currency.symbol,
						color = colorScheme.onSurface,
						style = textStyle,
						modifier = Modifier.offset(y = symbolOffset)
					)
					Spacer(Modifier.width(6.dp))
				}
			},
			onValueChange = {
				onValueChange(AppCtx.currency.formReader(it))
			},
			placeholder = if (hasPlaceholder) {
				{
					Text(
						text = AppCtx.currency.formWriter(0),
						color = colorScheme.onSurface,
						style = textStyle
					)
				}
			} else null,
			singleLine = true,
			keyboardOptions = KeyboardOptions(
				keyboardType = KeyboardType.NumberPassword,
				imeAction = ImeAction.Next
			),
			keyboardActions = KeyboardActions(
				onNext = { focusManager.moveFocus(FocusDirection.Down) }
			),
			modifier = modifier,
			textStyle = textStyle,
			shape = RoundedCornerShape(8.dp),
			colors = outlinedTextFieldColors().let {
				if (backgroundColor != null) {
					it.copy(
						focusedContainerColor = backgroundColor,
						unfocusedContainerColor = backgroundColor
					)
				} else {
					it
				}
			}
		)
	}
}