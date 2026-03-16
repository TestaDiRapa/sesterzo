package org.testadirapa.sesterzo.security

import org.testadirapa.sesterzo.model.UserSpaceRole

data class JWTClaims(
	val userId: String,
	val spaces: Map<String, UserSpaceRole>
)
