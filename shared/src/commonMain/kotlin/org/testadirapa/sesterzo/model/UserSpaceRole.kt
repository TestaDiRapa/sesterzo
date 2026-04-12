package org.testadirapa.sesterzo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class UserSpaceRole(val authLevel: Int) {
	@SerialName("a") Admin(authLevel = 0),
	@SerialName("u") User(authLevel = 1),
}