package org.testadirapa.sesterzo.cache

import com.juul.indexeddb.Database
import com.juul.indexeddb.Key
import org.testadirapa.sesterzo.cache.model.SpaceEntity
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.models.JsSpace
import org.testadirapa.sesterzo.models.toJs
import org.testadirapa.sesterzo.models.toKt

class JsSpacePersistentCache(
	private val database: Database
) : SpacePersistentCache {

	companion object {
		const val STORE_NAME = "space"
	}

	override suspend fun upsert(space: Space) {
		database.writeTransaction(STORE_NAME) {
			objectStore(STORE_NAME).put(space.toJs())
		}
	}

	override suspend fun upsertAll(spaces: List<Space>) {
		database.writeTransaction(STORE_NAME) {
			val store = objectStore(STORE_NAME)
			spaces.forEach { space ->
				store.put(space.toJs())
			}
		}
	}

	@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
	@OptIn(ExperimentalWasmJsInterop::class)
	override suspend fun getAll(): List<SpaceEntity> = database.transaction(STORE_NAME) {
		val store = objectStore(STORE_NAME)
		store.getAll().map { (it as JsSpace).toKt() }
	}

	@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
	override suspend fun getById(id: String): SpaceEntity? = database.transaction(STORE_NAME) {
		objectStore(STORE_NAME).get(Key(id)) as JsSpace?
	}?.toKt()

	override suspend fun clearAll() {
		database.writeTransaction(STORE_NAME) {
			objectStore(STORE_NAME).clear()
		}
	}

}