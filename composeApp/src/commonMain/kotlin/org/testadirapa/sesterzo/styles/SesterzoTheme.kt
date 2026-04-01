package org.testadirapa.sesterzo.styles

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import org.testadirapa.sesterzo.styles.colors.DarkColorScheme
import org.testadirapa.sesterzo.styles.colors.DarkSemanticColors
import org.testadirapa.sesterzo.styles.colors.LightColorScheme
import org.testadirapa.sesterzo.styles.colors.LightSemanticColors
import org.testadirapa.sesterzo.styles.colors.LocalSemanticColors
import org.testadirapa.sesterzo.styles.font.dmSansFontFamily
import org.testadirapa.sesterzo.styles.typography.sesterzoTypography

@Composable
fun SesterzoTheme(
	darkTheme: Boolean = false,
	content: @Composable () -> Unit
) {
	val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
	val semanticColors = if (darkTheme) DarkSemanticColors else LightSemanticColors

	CompositionLocalProvider(LocalSemanticColors provides semanticColors) {
		MaterialTheme(
			colorScheme = colorScheme,
			typography = sesterzoTypography(dmSansFontFamily()),
			content = content
		)
	}
}