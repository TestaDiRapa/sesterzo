package org.testadirapa.sesterzo.styles

import kotlinx.browser.window
import androidx.compose.runtime.Composable

@Composable
actual fun isDarkTheme(): Boolean =
	window.matchMedia("(prefers-color-scheme: dark)").matches