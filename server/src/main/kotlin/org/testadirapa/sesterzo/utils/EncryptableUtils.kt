package org.testadirapa.sesterzo.utils

import org.testadirapa.sesterzo.model.EncryptedData

private const val ENCRYPTED_SELF_MAX_SIZE = 1024 * 5

fun EncryptedData<*>.requireSizeIsUnderThreshold() {
	require(
		encryptedSelf?.let { it.length < ENCRYPTED_SELF_MAX_SIZE } ?: true
	) { "Encrypted data size exceeds $ENCRYPTED_SELF_MAX_SIZE bytes" }
}
