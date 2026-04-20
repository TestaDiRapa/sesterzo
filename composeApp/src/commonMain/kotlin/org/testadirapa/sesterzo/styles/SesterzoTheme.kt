package org.testadirapa.sesterzo.styles

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import org.testadirapa.sesterzo.styles.colors.DarkColorScheme
import org.testadirapa.sesterzo.styles.colors.DarkFinanceColors
import org.testadirapa.sesterzo.styles.colors.LightColorScheme
import org.testadirapa.sesterzo.styles.colors.LightFinanceColors
import org.testadirapa.sesterzo.styles.colors.LocalFinanceColors
import org.testadirapa.sesterzo.styles.font.dmSansFontFamily
import org.testadirapa.sesterzo.styles.typography.sesterzoTypography

@Composable
fun SesterzoTheme(
	darkTheme: Boolean = false,
	content: @Composable () -> Unit
) {
	val scheme = if (darkTheme) DarkColorScheme else LightColorScheme
	val finance = if (darkTheme) DarkFinanceColors else LightFinanceColors

	CompositionLocalProvider(LocalFinanceColors provides finance) {
		MaterialTheme(
			colorScheme = scheme,
			typography  = sesterzoTypography(dmSansFontFamily()),
			content     = content,
		)
	}
}