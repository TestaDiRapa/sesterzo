package org.testadirapa.sesterzo.styles.colors

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object SesterzoColors {
	// Gold (primary / brand / fixed expenses)
	val Gold50  = Color(0xFFFDF6DC)
	val Gold100 = Color(0xFFF5DC7A)
	val Gold200 = Color(0xFFE8B800)
	val Gold400 = Color(0xFFC9960C)   // ★ primary action
	val Gold600 = Color(0xFFA67C00)   // border, emphasis
	val Gold800 = Color(0xFF7A5C00)   // text on gold bg
	val Gold900 = Color(0xFF3D2E00)   // brand panel bg / dark surface

	// Green (savings / positive delta)
	val Green50  = Color(0xFFEAF3DE)
	val Green200 = Color(0xFF97C459)
	val Green400 = Color(0xFF1D9E75)
	val Green600 = Color(0xFF3B6D11)

	// Red (overspend / alerts)
	val Red50  = Color(0xFFFCEBEB)
	val Red200 = Color(0xFFF09595)
	val Red400 = Color(0xFFE24B4A)
	val Red600 = Color(0xFFA32D2D)

	// Terracotta (variable expenses)
	val Terra50  = Color(0xFFFAECE7)
	val Terra200 = Color(0xFFF0997B)
	val Terra400 = Color(0xFFD85A30)
	val Terra600 = Color(0xFF993C1D)

	// Warm stone neutrals
	val White    = Color(0xFFFFFFFF)
	val Stone50  = Color(0xFFF7F4EE)   // page background
	val Stone100 = Color(0xFFE8E4DA)   // dividers, borders
	val Stone400 = Color(0xFF9C9488)   // secondary text
	val Stone900 = Color(0xFF2E2A22)   // primary text

	// Dark mode surfaces (warm-tinted darks)
	val DarkSurface  = Color(0xFF1E1B14)
	val DarkSurface2 = Color(0xFF272318)
	val DarkSurface3 = Color(0xFF302B1E)
	val DarkBorder   = Color(0xFF3D3828)
	val DarkTextPrimary   = Color(0xFFF2EDD8)
	val DarkTextSecondary = Color(0xFF9C9070)
}

val LightColorScheme = lightColorScheme(
	primary = SesterzoColors.Gold400,
	onPrimary = SesterzoColors.Gold50,
	primaryContainer = SesterzoColors.Gold50,
	onPrimaryContainer = SesterzoColors.Gold800,

	secondary = SesterzoColors.Green400,
	onSecondary = SesterzoColors.White,
	secondaryContainer = SesterzoColors.Green50,
	onSecondaryContainer = SesterzoColors.Green600,

	tertiary = SesterzoColors.Terra400,
	onTertiary = SesterzoColors.White,
	tertiaryContainer = SesterzoColors.Terra50,
	onTertiaryContainer = SesterzoColors.Terra600,

	error = SesterzoColors.Red400,
	onError = SesterzoColors.White,
	errorContainer = SesterzoColors.Red50,
	onErrorContainer = SesterzoColors.Red600,

	background = SesterzoColors.Stone50,
	onBackground = SesterzoColors.Stone900,
	surface = SesterzoColors.White,
	onSurface = SesterzoColors.Stone900,
	surfaceVariant = SesterzoColors.Stone50,
	onSurfaceVariant = SesterzoColors.Stone400,
	outline = SesterzoColors.Stone100,
	outlineVariant = SesterzoColors.Stone100,
)

val DarkColorScheme = darkColorScheme(
	primary = SesterzoColors.Gold200,
	onPrimary = SesterzoColors.Gold900,
	primaryContainer = SesterzoColors.Gold800,
	onPrimaryContainer = SesterzoColors.Gold100,

	secondary = SesterzoColors.Green400,
	onSecondary = SesterzoColors.Gold900,
	secondaryContainer   = Color(0xFF0F3D2A),
	onSecondaryContainer = Color(0xFF9FE1CB),

	tertiary = SesterzoColors.Terra200,
	onTertiary = Color(0xFF4A1B0C),
	tertiaryContainer = Color(0xFF6B2C15),
	onTertiaryContainer = Color(0xFFF5C4B3),

	error = SesterzoColors.Red200,
	onError = Color(0xFF501313),
	errorContainer = Color(0xFF791F1F),
	onErrorContainer = Color(0xFFF7C1C1),

	background = SesterzoColors.DarkSurface,
	onBackground = SesterzoColors.DarkTextPrimary,
	surface = SesterzoColors.DarkSurface2,
	onSurface = SesterzoColors.DarkTextPrimary,
	surfaceVariant = SesterzoColors.DarkSurface3,
	onSurfaceVariant = SesterzoColors.DarkTextSecondary,
	outline = SesterzoColors.DarkBorder,
	outlineVariant = SesterzoColors.DarkBorder,
)