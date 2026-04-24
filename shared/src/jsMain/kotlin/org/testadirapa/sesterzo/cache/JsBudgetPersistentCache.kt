package org.testadirapa.sesterzo.cache

import com.juul.indexeddb.Database
import com.juul.indexeddb.Key
import com.juul.indexeddb.KeyPath
import com.juul.indexeddb.VersionChangeTransaction
import com.juul.indexeddb.only
import org.testadirapa.sesterzo.cache.model.CachedBudget
import org.testadirapa.sesterzo.model.EncryptedBudget
import org.testadirapa.sesterzo.model.EncryptedExpense
import org.testadirapa.sesterzo.models.JsBudget
import org.testadirapa.sesterzo.models.toJs
import org.testadirapa.sesterzo.models.toKt

class JsBudgetPersistentCache(
	database: Database
) : JsAbstractPersistentCache<EncryptedBudget, CachedBudget>(database), BudgetPersistentCache {

	companion object {
		private const val STORE_NAME = "budget"

		fun VersionChangeTransaction.initBudgetStorage(database: Database) = with(this) {
			val store = database.createObjectStore(STORE_NAME, KeyPath("id"))
			store.createIndex("year", KeyPath("year"), unique = false)
		}
	}

	override val storeName: String get() = STORE_NAME

	@OptIn(ExperimentalWasmJsInterop::class)
	override fun toJs(entity: EncryptedBudget): JsAny = entity.toJs()

	@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
	@OptIn(ExperimentalWasmJsInterop::class)
	override fun toKt(dbEntity: JsAny): CachedBudget = (dbEntity as JsBudget).toKt()

	override fun keyOf(entity: EncryptedBudget): Key = Key(entity.id)

	@OptIn(ExperimentalWasmJsInterop::class)
	override suspend fun getByYearInSpace(spaceId: String, year: Int): List<CachedBudget> = database.transaction(storeName) {
		objectStore(storeName)
			.index("year")
			.getAll(query = only(year))
			.mapNotNull { budget -> budget?.let { toKt(dbEntity = it) } }
			.filter { it.entity.spaceId == spaceId }
	}
}