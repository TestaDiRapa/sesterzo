package org.testadirapa.sesterzo.cache

import org.testadirapa.sesterzo.cache.model.SpaceEntity
import org.testadirapa.sesterzo.model.Space

interface SpacePersistentCache {
	suspend fun upsert(space: Space)
	suspend fun upsertAll(spaces: List<Space>)
	suspend fun getAll(): List<SpaceEntity>
	suspend fun getById(id: String): SpaceEntity?
	suspend fun clearAll()
}