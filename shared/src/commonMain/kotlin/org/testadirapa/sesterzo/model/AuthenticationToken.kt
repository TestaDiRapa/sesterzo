package org.testadirapa.sesterzo.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationToken(
	val token: String,
	val createdAt: Timestamp,
	val expiresAt: Timestamp
)
