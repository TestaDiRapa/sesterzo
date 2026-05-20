package org.testadirapa.sesterzo.cache

interface PersistenceOperator<W, R> {
	suspend fun upsert(entity: W)
	suspend fun getById(id: String): R?
	suspend fun getByIds(ids: List<String>): List<R>
	suspend fun getAll(): List<R>
	suspend fun clear(entity: W)
	suspend fun upsertAll(entities: List<W>)
	suspend fun clearAll()
}