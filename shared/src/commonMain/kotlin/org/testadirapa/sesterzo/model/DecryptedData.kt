package org.testadirapa.sesterzo.model

import kotlinx.serialization.json.JsonObject

interface DecryptedData<ENCRYPTED_TYPE> {

	fun getJsonToEncrypt(): JsonObject
	fun toEncryptedEntity(encryptedSelf: Base64String?) : ENCRYPTED_TYPE
}