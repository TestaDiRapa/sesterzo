package org.testadirapa.sesterzo.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface AccessKey {
	val encryptedKey: Base64String
}

@Serializable
data class UserAccessKey(
	val accessLevel: UserSpaceRole,
	override val encryptedKey: Base64String,
) : AccessKey

@Serializable
data class SharedAccessKey(
	val issuedAt: Timestamp,
	val expiresAt: Timestamp,
	override val encryptedKey: Base64String,
) : AccessKey