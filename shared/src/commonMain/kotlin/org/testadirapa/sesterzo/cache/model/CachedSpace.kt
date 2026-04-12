package org.testadirapa.sesterzo.cache.model

import kotlinx.serialization.Serializable
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.model.Timestamp

@Serializable
data class CachedSpace(
	override val id: String,
	override val entity: Space,
	override val insertedAt: Timestamp
) : EntityWithInsertionTs<Space>
