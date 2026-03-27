package org.testadirapa.sesterzo.model.dto

import com.icure.kerberus.Solution
import kotlinx.serialization.Serializable

@Serializable
data class OttData(
	override val email: String,
	val captchaSolution: Solution
): DataWithEmail