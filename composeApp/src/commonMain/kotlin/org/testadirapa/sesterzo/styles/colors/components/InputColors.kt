package org.testadirapa.sesterzo.styles.colors.components

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import org.testadirapa.sesterzo.styles.colors.SesterzoColors

@Composable
fun outlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
	focusedBorderColor = SesterzoColors.Blue400,
	unfocusedBorderColor = SesterzoColors.Gray100,
	errorBorderColor = SesterzoColors.Red400,
	focusedLabelColor = SesterzoColors.Blue400,
	unfocusedLabelColor = SesterzoColors.Gray400,
	cursorColor = SesterzoColors.Blue400,
)