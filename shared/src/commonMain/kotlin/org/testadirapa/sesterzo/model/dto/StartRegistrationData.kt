package org.testadirapa.sesterzo.model.dto

import com.icure.kerberus.Solution
import kotlinx.serialization.Serializable

@Serializable
data class StartRegistrationData(
	val email: String,
	val name: String,
	val captchaSolution: Solution
)
