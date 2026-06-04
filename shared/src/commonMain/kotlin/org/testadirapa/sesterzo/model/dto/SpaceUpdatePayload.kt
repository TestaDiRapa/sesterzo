package org.testadirapa.sesterzo.model.dto

import kotlinx.serialization.Serializable
import org.testadirapa.sesterzo.model.RGBColor

@Serializable
data class SpaceUpdatePayload(
	val name: String,
	val color: RGBColor,
)