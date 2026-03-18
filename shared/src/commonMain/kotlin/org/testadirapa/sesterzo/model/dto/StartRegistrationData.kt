package org.testadirapa.sesterzo.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class StartRegistrationData(
	val email: String,
	val name: String
)
