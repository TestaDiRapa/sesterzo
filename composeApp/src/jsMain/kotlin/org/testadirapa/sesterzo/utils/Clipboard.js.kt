package org.testadirapa.sesterzo.utils

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry

@OptIn(ExperimentalComposeUiApi::class)
actual fun newClipEntry(label: String, text: String): ClipEntry = ClipEntry.withPlainText(text)