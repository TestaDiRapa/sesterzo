package org.testadirapa.sesterzo.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class CompleteRegistrationData(
	val processId: String,
	val token: String
)
