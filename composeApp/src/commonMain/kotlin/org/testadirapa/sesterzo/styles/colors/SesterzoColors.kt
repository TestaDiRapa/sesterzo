package org.testadirapa.sesterzo.styles.colors

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object SesterzoColors {
	// — Neutrals (warm) —
	val Bg0         = Color(0xFF0E0D0B)
	val Surface1    = Color(0xFF1A1714)
	val Surface2    = Color(0xFF221E1A)
	val Surface3    = Color(0xFF2C2823)
	val OutlineVar  = Color(0xFF332E27)
	val Outline     = Color(0xFF4A4239)
	val TextTert    = Color(0xFF8A8174)
	val TextSec     = Color(0xFFC8BEAE)
	val TextPrim    = Color(0xFFF4EFE6)

	// — Amber —
	val Amber50     = Color(0xFFFBF3DF)
	val Amber100    = Color(0xFFF5E1A8)
	val Amber200    = Color(0xFFEECB72)
	val Amber300    = Color(0xFFE8B547) // primary
	val Amber400    = Color(0xFFC9972E)
	val Amber500    = Color(0xFF9F761F)
	val AmberContainer = Color(0xFF4A3800)
	val OnAmber     = Color(0xFF1A1200)

	// — Semantic —
	val Income300   = Color(0xFF86C8A2)
	val Income500   = Color(0xFF3F8E5E)
	val Spent300    = Color(0xFFE88B7C)
	val Spent500    = Color(0xFFB4513F)
	val SpentContainer = Color(0xFF3A1611)
	val Saved300    = Color(0xFF9EC9B8)
	val Saved500    = Color(0xFF527D6D)

	// — Space identities —
	val SpacePersonal = Amber300
	val SpaceFamily   = Color(0xFF9BD1B8)
	val SpaceSide     = Color(0xFFC3A8E8)
	val SpaceTravel   = Color(0xFFE89B7C)

	// Light colors
	val LightBg0        = Color(0xFFFAF6EE)
	val LightSurface1   = Color(0xFFFFFFFF)
	val LightSurface2   = Color(0xFFF3EEE2)
	val LightSurface3   = Color(0xFFEBE3D2)
	val LightOutlineVar = Color(0xFFE4DCC8)
	val LightOutline    = Color(0xFFCFC4A8)
	val LightTextTert   = Color(0xFF7A7060)
	val LightTextSec    = Color(0xFF4E4638)
	val LightTextPrim   = Color(0xFF1A1714)

	// — Light amber (darkened for AA on white) —
	val LightAmber50    = Color(0xFFFDF7E4)
	val LightAmber100   = Color(0xFFF8E9B9)
	val LightAmber300   = Color(0xFFC9972E) // primary
	val LightAmber500   = Color(0xFF7D5A10)

	// — Light semantic —
	val LightIncome300  = Color(0xFF4EA673)
	val LightIncome500  = Color(0xFF2F6B48)
	val LightSpent300   = Color(0xFFC75A47)
	val LightSpent500   = Color(0xFF8C3B2B)
	val LightSpentContainer = Color(0xFFFCE8E3)
	val LightSaved300   = Color(0xFF5A8F7E)
	val LightSaved500   = Color(0xFF3B6655)

	// — Light space identities —
	val LightSpacePersonal = LightAmber300
	val LightSpaceFamily   = Color(0xFF4EA673)
	val LightSpaceSide     = Color(0xFF7F62B3)
	val LightSpaceTravel   = Color(0xFFC75A47)
}

val LightColorScheme = lightColorScheme(
	primary = SesterzoColors.LightAmber300,
	onPrimary = Color.White,
	primaryContainer = SesterzoColors.LightAmber50,
	onPrimaryContainer = SesterzoColors.LightAmber500,

	tertiaryContainer = SesterzoColors.LightAmber300,
	onTertiaryContainer = SesterzoColors.LightAmber100,

	onTertiary = SesterzoColors.LightTextTert,

	background = SesterzoColors.LightBg0,
	onBackground = SesterzoColors.LightTextPrim,
	surface = SesterzoColors.LightSurface1,
	onSurface = SesterzoColors.LightTextPrim,

	surfaceVariant = SesterzoColors.LightSurface2,
	onSurfaceVariant = SesterzoColors.LightTextSec,
	surfaceContainer = SesterzoColors.LightSurface2,
	surfaceContainerHigh = SesterzoColors.LightSurface3,
	outline = SesterzoColors.LightOutline,
	outlineVariant = SesterzoColors.LightOutlineVar,
	error = SesterzoColors.LightSpent300,
	onError = Color.White,
	errorContainer = SesterzoColors.LightSpentContainer,
	onErrorContainer = SesterzoColors.LightSpent500,
)

val DarkColorScheme = darkColorScheme(
	primary = SesterzoColors.Amber300,
	onPrimary = SesterzoColors.OnAmber,
	primaryContainer = SesterzoColors.AmberContainer,
	onPrimaryContainer = SesterzoColors.Amber100,

	secondary = SesterzoColors.Saved300,
	onSecondary = SesterzoColors.OnAmber,

	onTertiary = SesterzoColors.TextTert,
	tertiaryContainer = SesterzoColors.Amber500,
	onTertiaryContainer = SesterzoColors.Amber50,

	background = SesterzoColors.Bg0,
	onBackground = SesterzoColors.TextPrim,
	surface = SesterzoColors.Surface1,
	onSurface = SesterzoColors.TextPrim,
	surfaceVariant = SesterzoColors.Surface2,
	onSurfaceVariant = SesterzoColors.TextSec,
	surfaceContainer = SesterzoColors.Surface2,
	surfaceContainerHigh = SesterzoColors.Surface3,

	outline = SesterzoColors.Outline,
	outlineVariant = SesterzoColors.OutlineVar,

	error = SesterzoColors.Spent300,
	onError = SesterzoColors.OnAmber,
	errorContainer = SesterzoColors.SpentContainer,
	onErrorContainer = SesterzoColors.Spent300,
)