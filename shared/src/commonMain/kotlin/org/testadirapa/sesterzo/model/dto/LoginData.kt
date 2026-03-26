package org.testadirapa.sesterzo.model.dto

import com.icure.kerberus.Solution
import kotlinx.serialization.Serializable

@Serializable
data class LoginData(
	val email: String,
	val token: String,
	val captchaSolution: Solution
)