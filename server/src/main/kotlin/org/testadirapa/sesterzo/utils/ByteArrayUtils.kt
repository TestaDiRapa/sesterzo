package org.testadirapa.sesterzo.utils

import com.icure.kryptom.utils.base64Decode
import org.testadirapa.sesterzo.model.Base64String

private const val BLOB_THRESHOLD = 10 * 1024 * 1024

fun Base64String?.isSizeUnderThreshold(): Boolean =
	this == null || base64Decode(this).size < BLOB_THRESHOLD