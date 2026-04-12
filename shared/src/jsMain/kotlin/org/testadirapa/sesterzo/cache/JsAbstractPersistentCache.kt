package org.testadirapa.sesterzo.cache

import com.juul.indexeddb.Database
import com.juul.indexeddb.Key

abstract class JsAbstractPersistentCache<W, R>(
	protected val database: Database
) : PersistenceOperator<W, R> {

	abstract val storeName: String

	@OptIn(ExperimentalWasmJsInterop::class)
	abstract fun toJs(entity: W): JsAny

	@OptIn(ExperimentalWasmJsInterop::class)
	abstract fun toKt(dbEntity: JsAny): R

	abstract fun keyOf(entity: W): Key

	@OptIn(ExperimentalWasmJsInterop::class)
	override suspend fun upsert(entity: W) {
		database.writeTransaction(storeName) {
			objectStore(storeName).put(toJs(entity))
		}
	}

	@OptIn(ExperimentalWasmJsInterop::class)
	override suspend fun upsertAll(entities: List<W>) {
		database.writeTransaction(storeName) {
			val store = objectStore(storeName)
			entities.forEach { entity ->
				store.put(toJs(entity))
			}
		}
	}

	@OptIn(ExperimentalWasmJsInterop::class)
	override suspend fun getAll(): List<R> = database.transaction(storeName) {
		val store = objectStore(storeName)
		store.getAll().mapNotNull { dbElement -> dbElement?.let { toKt(dbEntity = it) } }
	}

	@OptIn(ExperimentalWasmJsInterop::class)
	override suspend fun getById(id: String): R? = database.transaction(storeName) {
		objectStore(storeName).get(Key(id))
	}?.let { toKt(it) }

	override suspend fun clearAll() {
		database.writeTransaction(storeName) {
			objectStore(storeName).clear()
		}
	}

	override suspend fun clear(entity: W) {
		database.writeTransaction(storeName) {
			objectStore(storeName).delete(keyOf(entity))
		}
	}

}