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
	fixedExpense = SesterzoColors.Gold400,
	fixedExpenseContainer = SesterzoColors.Gold50,
	onFixedExpenseContainer = SesterzoColors.Gold800,

	variableExpense = SesterzoColors.Terra400,
	variableExpenseContainer = SesterzoColors.Terra50,
	onVariableExpenseContainer = SesterzoColors.Terra600,

	savings = SesterzoColors.Green400,
	savingsContainer = SesterzoColors.Green50,
	onSavingsContainer = SesterzoColors.Green600,

	positiveAmount = SesterzoColors.Green400,
	negativeAmount = SesterzoColors.Red400,
	unplannedAmount = SesterzoColors.Terra400,

	divider = SesterzoColors.Stone100,
	cardSurface = SesterzoColors.White,
)

val DarkSemanticColors = SesterzoSemanticColors(
	fixedExpense = SesterzoColors.Gold200,
	fixedExpenseContainer = SesterzoColors.Gold800,
	onFixedExpenseContainer = SesterzoColors.Gold100,

	variableExpense  = SesterzoColors.Terra200,
	variableExpenseContainer = Color(0xFF6B2C15),
	onVariableExpenseContainer = Color(0xFFF5C4B3),

	savings = SesterzoColors.Green400,
	savingsContainer = Color(0xFF0F3D2A),
	onSavingsContainer = Color(0xFF9FE1CB),

	positiveAmount = Color(0xFF5DCAA5),
	negativeAmount = SesterzoColors.Red200,
	unplannedAmount = SesterzoColors.Terra200,

	divider = SesterzoColors.DarkBorder,
	cardSurface = SesterzoColors.DarkSurface2,
)

val MaterialTheme.semantic: SesterzoSemanticColors
	@Composable get() = LocalSemanticColors.current