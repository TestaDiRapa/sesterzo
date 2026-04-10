package org.testadirapa.sesterzo.cache.model

import kotlinx.serialization.Serializable
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.model.Timestamp

@Serializable
data class SpaceEntity(
	val id: String,
	val space: Space,
	val insertedAt: Timestamp
)
