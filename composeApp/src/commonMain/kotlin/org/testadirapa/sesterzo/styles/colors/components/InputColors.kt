package org.testadirapa.sesterzo.styles.colors.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import org.testadirapa.sesterzo.styles.colors.SesterzoColors

@Composable
fun outlinedTextFieldColors(): TextFieldColors =
	if(isSystemInDarkTheme()) {
		OutlinedTextFieldDefaults.colors(
			focusedBorderColor   = SesterzoColors.Amber300,
			unfocusedBorderColor = SesterzoColors.Outline,
			errorBorderColor     = SesterzoColors.Spent300,
			focusedLabelColor    = SesterzoColors.Amber300,
			unfocusedLabelColor  = SesterzoColors.TextTert,
			cursorColor          = SesterzoColors.Amber300,
			errorCursorColor     = SesterzoColors.Spent300,
			focusedTextColor     = SesterzoColors.TextPrim,
			unfocusedTextColor   = SesterzoColors.TextPrim,
			disabledTextColor    = SesterzoColors.TextTert,
			focusedContainerColor    = SesterzoColors.Surface2,
			unfocusedContainerColor  = SesterzoColors.Surface1,
			disabledContainerColor   = SesterzoColors.Surface1,
			errorContainerColor      = SesterzoColors.Surface1,
		)
	} else {
		OutlinedTextFieldDefaults.colors(
			focusedBorderColor       = SesterzoColors.LightAmber300,
			unfocusedBorderColor     = SesterzoColors.LightOutline,
			errorBorderColor         = SesterzoColors.LightSpent300,
			focusedLabelColor        = SesterzoColors.LightAmber500,
			unfocusedLabelColor      = SesterzoColors.LightTextTert,
			cursorColor              = SesterzoColors.LightAmber300,
			focusedContainerColor    = SesterzoColors.LightSurface1,
			unfocusedContainerColor  = SesterzoColors.LightSurface1,
		)
	}
