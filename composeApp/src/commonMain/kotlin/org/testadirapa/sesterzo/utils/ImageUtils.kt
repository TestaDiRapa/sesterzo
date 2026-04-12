package org.testadirapa.sesterzo.utils

import androidx.compose.ui.graphics.ImageBitmap

const val MAX_IMAGE_SIZE_BYTES = 10 * 1024 * 1024L

expect fun ByteArray.toImageBitmap(): ImageBitmap
