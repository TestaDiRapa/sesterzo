package org.testadirapa.sesterzo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class UserSpaceRole {
	@SerialName("a") Admin,
	@SerialName("u") User
}