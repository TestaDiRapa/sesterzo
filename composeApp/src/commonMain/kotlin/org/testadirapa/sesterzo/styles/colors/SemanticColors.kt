package org.testadirapa.sesterzo.styles.colors

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class SesterzoSemanticColors(
	val fixedExpense: Color,
	val fixedExpenseContainer: Color,
	val onFixedExpenseContainer: Color,

	val variableExpense: Color,
	val variableExpenseContainer: Color,
	val onVariableExpenseContainer: Color,

	val savings: Color,
	val savingsContainer: Color,
	val onSavingsContainer: Color,

	val positiveAmount: Color,
	val negativeAmount: Color,
	val unplannedAmount: Color,

	val divider: Color,
	val cardSurface: Color,
)

val LocalSemanticColors = staticCompositionLocalOf<SesterzoSemanticColors> {
	error("No SesterzoSemanticColors provided")
}

val LightSemanticColors = SesterzoSemanticColors(
	fixedExpense          = SesterzoColors.Blue600,
	fixedExpenseContainer = SesterzoColors.Blue50,
	onFixedExpenseContainer = SesterzoColors.Blue800,

	variableExpense          = SesterzoColors.Amber400,
	variableExpenseContainer = SesterzoColors.Amber50,
	onVariableExpenseContainer = SesterzoColors.Amber600,

	savings          = SesterzoColors.Green400,
	savingsContainer = SesterzoColors.Green50,
	onSavingsContainer = SesterzoColors.Green600,

	positiveAmount  = SesterzoColors.Green400,
	negativeAmount  = SesterzoColors.Red400,
	unplannedAmount = SesterzoColors.Amber400,

	divider     = SesterzoColors.Gray100,
	cardSurface = SesterzoColors.White,
)

val DarkSemanticColors = SesterzoSemanticColors(
	fixedExpense          = SesterzoColors.Blue400,
	fixedExpenseContainer = SesterzoColors.Blue800,
	onFixedExpenseContainer = SesterzoColors.Blue100,

	variableExpense          = SesterzoColors.Amber200,
	variableExpenseContainer = Color(0xFF5A3208),
	onVariableExpenseContainer = Color(0xFFFAC775),

	savings          = SesterzoColors.Green400,
	savingsContainer = Color(0xFF0F3D2A),
	onSavingsContainer = Color(0xFF9FE1CB),

	positiveAmount  = Color(0xFF5DCAA5),
	negativeAmount  = SesterzoColors.Red200,
	unplannedAmount = SesterzoColors.Amber200,

	divider     = SesterzoColors.DarkBorder,
	cardSurface = SesterzoColors.DarkSurface2,
)
val MaterialTheme.semantic: SesterzoSemanticColors
	@Composable get() = LocalSemanticColors.current