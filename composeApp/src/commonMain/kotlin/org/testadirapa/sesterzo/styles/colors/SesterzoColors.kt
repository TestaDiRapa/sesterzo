package org.testadirapa.sesterzo.styles.colors

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object SesterzoColors {
	// Blue (primary / fixed expenses)
	val Blue50  = Color(0xFFE6F1FB)
	val Blue100 = Color(0xFFB5D4F4)
	val Blue400 = Color(0xFF378ADD)
	val Blue600 = Color(0xFF185FA5)   // ★ primary action
	val Blue800 = Color(0xFF0C447C)
	val Blue900 = Color(0xFF042C53)   // brand panel bg

	// Green (savings / positive)
	val Green50  = Color(0xFFEAF3DE)
	val Green200 = Color(0xFF97C459)
	val Green400 = Color(0xFF1D9E75)  // ★ savings dots, positive delta
	val Green600 = Color(0xFF3B6D11)

	// Red (overspend / alerts)
	val Red50  = Color(0xFFFCEBEB)
	val Red200 = Color(0xFFF09595)
	val Red400 = Color(0xFFE24B4A)    // ★ negative amounts
	val Red600 = Color(0xFFA32D2D)

	// Amber (variable expenses / warnings)
	val Amber50  = Color(0xFFFAEEDA)
	val Amber200 = Color(0xFFEF9F27)
	val Amber400 = Color(0xFFBA7517)  // ★ variable dots
	val Amber600 = Color(0xFF854F0B)

	// Neutrals
	val White    = Color(0xFFFFFFFF)
	val Gray50   = Color(0xFFF5F5F3)   // page background
	val Gray100  = Color(0xFFE8E8E5)   // dividers, borders
	val Gray400  = Color(0xFF888780)   // secondary text
	val Gray900  = Color(0xFF2C2C2A)   // primary text

	// Dark mode surfaces
	val DarkSurface    = Color(0xFF1C1C1A)
	val DarkSurface2   = Color(0xFF252523)
	val DarkSurface3   = Color(0xFF2E2E2B)
	val DarkBorder     = Color(0xFF3A3A37)
	val DarkTextPrimary   = Color(0xFFF0EFEA)
	val DarkTextSecondary = Color(0xFF9E9D97)
}

val LightColorScheme = lightColorScheme(
	primary            = SesterzoColors.Blue600,
	onPrimary          = SesterzoColors.White,
	primaryContainer   = SesterzoColors.Blue50,
	onPrimaryContainer = SesterzoColors.Blue800,

	secondary            = SesterzoColors.Green400,
	onSecondary          = SesterzoColors.White,
	secondaryContainer   = SesterzoColors.Green50,
	onSecondaryContainer = SesterzoColors.Green600,

	tertiary            = SesterzoColors.Amber400,
	onTertiary          = SesterzoColors.White,
	tertiaryContainer   = SesterzoColors.Amber50,
	onTertiaryContainer = SesterzoColors.Amber600,

	error            = SesterzoColors.Red400,
	onError          = SesterzoColors.White,
	errorContainer   = SesterzoColors.Red50,
	onErrorContainer = SesterzoColors.Red600,

	background    = SesterzoColors.Gray50,
	onBackground  = SesterzoColors.Gray900,
	surface       = SesterzoColors.White,
	onSurface     = SesterzoColors.Gray900,
	surfaceVariant   = SesterzoColors.Gray50,
	onSurfaceVariant = SesterzoColors.Gray400,
	outline          = SesterzoColors.Gray100,
	outlineVariant   = SesterzoColors.Gray100,
)

val DarkColorScheme = darkColorScheme(
	primary            = SesterzoColors.Blue400,
	onPrimary          = SesterzoColors.Blue900,
	primaryContainer   = SesterzoColors.Blue800,
	onPrimaryContainer = SesterzoColors.Blue100,

	secondary            = SesterzoColors.Green400,
	onSecondary          = SesterzoColors.Blue900,
	secondaryContainer   = Color(0xFF0F3D2A),
	onSecondaryContainer = Color(0xFF9FE1CB),

	tertiary            = SesterzoColors.Amber200,
	onTertiary          = Color(0xFF412402),
	tertiaryContainer   = Color(0xFF5A3208),
	onTertiaryContainer = Color(0xFFFAC775),

	error            = SesterzoColors.Red200,
	onError          = Color(0xFF501313),
	errorContainer   = Color(0xFF791F1F),
	onErrorContainer = Color(0xFFF7C1C1),

	background    = SesterzoColors.DarkSurface,
	onBackground  = SesterzoColors.DarkTextPrimary,
	surface       = SesterzoColors.DarkSurface2,
	onSurface     = SesterzoColors.DarkTextPrimary,
	surfaceVariant   = SesterzoColors.DarkSurface3,
	onSurfaceVariant = SesterzoColors.DarkTextSecondary,
	outline          = SesterzoColors.DarkBorder,
	outlineVariant   = SesterzoColors.DarkBorder,
)