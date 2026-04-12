package org.testadirapa.sesterzo.model

import kotlinx.serialization.json.JsonObject

interface EncryptedData<DECRYPTED_TYPE> {
	val encryptedSelf: Base64String?

	fun toDecryptedData(decryptedFields: JsonObject): DECRYPTED_TYPE
}