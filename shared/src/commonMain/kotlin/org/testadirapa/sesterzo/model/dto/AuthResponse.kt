package org.testadirapa.sesterzo.model.dto

import kotlinx.serialization.Serializable

/**
 * Contains all the data returned on a successful authentication.
 */
@Serializable
data class AuthResponse(
	val jwt: String,
	val refreshJwt: String
)
