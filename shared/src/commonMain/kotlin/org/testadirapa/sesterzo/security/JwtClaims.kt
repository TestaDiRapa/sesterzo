package org.testadirapa.sesterzo.security

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.testadirapa.sesterzo.model.UserSpaceRole
import org.testadirapa.sesterzo.security.UserJwtClaims.Companion.SPACES_KEY
import org.testadirapa.sesterzo.security.UserJwtClaims.Companion.USER_ID_KEY

@Serializable
data class JwtClaims(
	@SerialName(USER_ID_KEY) override val userId: String,
	@SerialName(SPACES_KEY) override val spaces: Map<String, UserSpaceRole>
) : UserJwtClaims

