package org.testadirapa.sesterzo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecoveryData(
	@SerialName("_id") val id: String,
	val createdAt: Timestamp,
	val expiresAt: Timestamp,
	val encryptedKey: Base64String,
)