package org.testadirapa.sesterzo.security

import org.testadirapa.sesterzo.model.UserSpaceRole

interface UserJwtClaims {
	val userId: String
	val spaces: Map<String, UserSpaceRole>

	companion object {
		const val USER_ID_KEY = "uId"
		const val SPACES_KEY = "s"
	}
}