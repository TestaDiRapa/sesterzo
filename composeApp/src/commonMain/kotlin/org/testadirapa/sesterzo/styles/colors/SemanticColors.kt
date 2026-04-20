package org.testadirapa.sesterzo.styles.colors

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class FinanceColors(
	val income: Color,
	val spent: Color,
	val saved: Color,
	val onTrackBg: Color,
	val overBg: Color,
	val savedBg: Color,
)

val LocalFinanceColors = staticCompositionLocalOf<FinanceColors> {
	error("No SesterzoSemanticColors provided")
}

val DarkFinanceColors = FinanceColors(
	income    = SesterzoColors.Income300,
	spent     = SesterzoColors.Spent300,
	saved     = SesterzoColors.Saved300,
	onTrackBg = Color(0x1F86C8A2),
	overBg    = Color(0x1FE88B7C),
	savedBg   = Color(0x1F9EC9B8),
)

val LightFinanceColors = FinanceColors(
	income    = SesterzoColors.LightIncome500,    // text uses 500 on white
	spent     = SesterzoColors.LightSpent500,
	saved     = SesterzoColors.LightSaved500,
	onTrackBg = Color(0xFFE6F2EB), // solid 8–10% tint
	overBg    = Color(0xFFFCE8E3),
	savedBg   = Color(0xFFE7EFEB),
)


val MaterialTheme.finance: FinanceColors
	@Composable @ReadOnlyComposable
	get() = LocalFinanceColors.current