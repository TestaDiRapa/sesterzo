package org.testadirapa.sesterzo.model

import kotlinx.serialization.Serializable

@Serializable
data class RGBColor(
	val red: Int,
	val green: Int,
	val blue: Int,
)
