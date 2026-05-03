package org.testadirapa.sesterzo.cache

import com.juul.indexeddb.Database
import com.juul.indexeddb.Key
import com.juul.indexeddb.KeyPath
import com.juul.indexeddb.VersionChangeTransaction
import com.juul.indexeddb.only
import org.testadirapa.sesterzo.model.EncryptedEntry
import org.testadirapa.sesterzo.models.JsEntry
import org.testadirapa.sesterzo.models.toJs
import org.testadirapa.sesterzo.models.toKt

class JsEntryPersistentCache(
	database: Database
) : JsAbstractPersistentCache<EncryptedEntry, EncryptedEntry>(database), EntryPersistentCache {

	companion object {
		private const val STORE_NAME = "expense"

		fun VersionChangeTransaction.initExpenseStorage(database: Database) = with(this) {
			val store = database.createObjectStore(STORE_NAME, KeyPath("id"))
			store.createIndex("budgetCoordinates", KeyPath("budgetCoordinates"), unique = false)
			store.createIndex("updated", KeyPath("updated"), unique = false)
		}
	}

	override val storeName: String get() = STORE_NAME

	@OptIn(ExperimentalWasmJsInterop::class)
	override fun toJs(entity: EncryptedEntry): JsAny = entity.toJs()

	@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
	@OptIn(ExperimentalWasmJsInterop::class)
	override fun toKt(dbEntity: JsAny): EncryptedEntry = (dbEntity as JsEntry).toKt()

	override fun keyOf(entity: EncryptedEntry): Key = Key(entity.id)

	@OptIn(ExperimentalWasmJsInterop::class)
	override suspend fun getAllForBudgetInSpace(spaceId: String, budgetId: String): List<EncryptedEntry> = database.transaction(storeName) {
		objectStore(storeName)
			.index("budgetCoordinates")
			.getAll(query = only("$spaceId-$budgetId"))
			.mapNotNull { dbElement -> dbElement?.let { toKt(dbEntity = it) } }
	}

}