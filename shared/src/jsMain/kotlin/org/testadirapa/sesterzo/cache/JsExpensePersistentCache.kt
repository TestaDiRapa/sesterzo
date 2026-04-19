package org.testadirapa.sesterzo.cache

import com.juul.indexeddb.Database
import com.juul.indexeddb.Key
import com.juul.indexeddb.KeyPath
import com.juul.indexeddb.VersionChangeTransaction
import com.juul.indexeddb.only
import org.testadirapa.sesterzo.model.EncryptedExpense
import org.testadirapa.sesterzo.models.JsExpense
import org.testadirapa.sesterzo.models.toJs
import org.testadirapa.sesterzo.models.toKt

class JsExpensePersistentCache(
	database: Database
) : JsAbstractPersistentCache<EncryptedExpense, EncryptedExpense>(database), ExpensePersistentCache {

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
	override fun toJs(entity: EncryptedExpense): JsAny = entity.toJs()

	@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
	@OptIn(ExperimentalWasmJsInterop::class)
	override fun toKt(dbEntity: JsAny): EncryptedExpense = (dbEntity as JsExpense).toKt()

	override fun keyOf(entity: EncryptedExpense): Key = Key(entity.id)

	@OptIn(ExperimentalWasmJsInterop::class)
	override suspend fun getAllForBudgetInSpace(spaceId: String, budgetId: String): List<EncryptedExpense> = database.transaction(storeName) {
		objectStore(storeName)
			.index("budgetCoordinates")
			.getAll(query = only("$spaceId-$budgetId"))
			.mapNotNull { dbElement -> dbElement?.let { toKt(dbEntity = it) } }
	}

}