package org.testadirapa.sesterzo.styles

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
actual fun isDarkTheme(): Boolean = isSystemInDarkTheme()