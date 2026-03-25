package org.testadirapa.sesterzo.model.dto

import com.icure.kerberus.Challenge
import kotlinx.serialization.Serializable

@Serializable
data class CaptchaChallenge(
	val input: String,
	val challenge: Challenge
)
