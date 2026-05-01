package org.testadirapa.sesterzo.utils

import androidx.compose.ui.graphics.ImageBitmap
import com.icure.kryptom.utils.base64Decode
import org.testadirapa.sesterzo.model.Base64String
import kotlin.io.encoding.Base64

const val MAX_IMAGE_SIZE_BYTES = 10 * 1024 * 1024L

expect fun ByteArray.toImageBitmap(): ImageBitmap

fun Base64String.toImageBitmap(): ImageBitmap = base64Decode(this).toImageBitmap()