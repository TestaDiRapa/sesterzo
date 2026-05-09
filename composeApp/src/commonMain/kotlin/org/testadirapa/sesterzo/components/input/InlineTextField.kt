package org.testadirapa.sesterzo.components.input

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.testadirapa.sesterzo.models.FormValue
import org.testadirapa.sesterzo.styles.colors.SesterzoColors.TextPrim
import org.testadirapa.sesterzo.styles.colors.SesterzoColors.TextTert

@Composable
fun InlineTextField(
	value: FormValue<String>,
	placeholder: String,
	onValueChange: (String) -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	imeAction: ImeAction = ImeAction.Next,
	onImeAction: (() -> Unit)? = null,
) {
	val focusManager = LocalFocusManager.current
	val interaction = remember { MutableInteractionSource() }
	val isFocused by interaction.collectIsFocusedAsState()

	val text = value.value.orEmpty()
	val showError = value.displayError

	val underline = when {
		showError -> MaterialTheme.colorScheme.error
		isFocused -> MaterialTheme.colorScheme.primary
		else -> Color.Transparent
	}

	val textStyle = LocalTextStyle.current.merge(
		TextStyle(
			color = if (enabled) TextPrim else TextPrim.copy(alpha = 0.4f),
			fontSize = 14.sp,
			fontWeight = FontWeight.Medium,
		)
	)

	BasicTextField(
		value = text,
		onValueChange = onValueChange,
		enabled = enabled,
		singleLine = true,
		textStyle = textStyle,
		cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
		interactionSource = interaction,
		keyboardOptions = KeyboardOptions(imeAction = imeAction),
		keyboardActions = KeyboardActions(
			onNext = { onImeAction?.invoke() ?: focusManager.moveFocus(FocusDirection.Down) },
			onDone = { onImeAction?.invoke() ?: focusManager.clearFocus() },
		),
		modifier = modifier
			.heightIn(min = 28.dp)
			.drawUnderline(underline),
		decorationBox = { inner ->
			Box(contentAlignment = Alignment.CenterStart) {
				if (text.isEmpty()) {
					Text(
						text = placeholder,
						style = textStyle.copy(color = TextTert),
					)
				}
				inner()
			}
		},
	)
}

// 1 dp underline drawn flush with the bottom of the row — animates on focus
private fun Modifier.drawUnderline(color: Color) = this.then(
	Modifier.drawBehind {
		if (color.alpha == 0f) return@drawBehind
		val y = size.height - 0.5.dp.toPx()
		drawLine(
			color = color,
			start = androidx.compose.ui.geometry.Offset(0f, y),
			end   = androidx.compose.ui.geometry.Offset(size.width, y),
			strokeWidth = 1.dp.toPx(),
		)
	}
)