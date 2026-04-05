package org.testadirapa.sesterzo.utils

import android.content.ClipData
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.toClipEntry

@RequiresApi(Build.VERSION_CODES.R)
actual fun newClipEntry(label: String, text: String): ClipEntry =
	ClipData.newPlainText(label, text).toClipEntry()