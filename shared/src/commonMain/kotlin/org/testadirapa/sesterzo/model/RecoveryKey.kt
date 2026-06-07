package org.testadirapa.sesterzo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecoveryKey(
	@SerialName("_id") override val id: String,
	val expiresAt: Timestamp?,
	val owner: String,
	val receiver: String,
	val encryptedKey: Base64String
) : Identifiable
