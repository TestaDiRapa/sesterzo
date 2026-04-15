package org.testadirapa.sesterzo.cache

import com.juul.indexeddb.Database
import com.juul.indexeddb.Key
import com.juul.indexeddb.KeyPath
import com.juul.indexeddb.VersionChangeTransaction
import org.testadirapa.sesterzo.cache.model.CachedBudgetElement
import org.testadirapa.sesterzo.model.EncryptedBudgetElement
import org.testadirapa.sesterzo.models.JsBudgetElement
import org.testadirapa.sesterzo.models.toJs
import org.testadirapa.sesterzo.models.toKt

class JsBudgetElementPersistentCache(
	database: Database
) : JsAbstractPersistentCache<EncryptedBudgetElement, CachedBudgetElement>(database), BudgetElementPersistentCache {

	companion object {
		private const val STORE_NAME = "budgetElement"

		fun VersionChangeTransaction.initBudgetElementStorage(database: Database) = with(this) {
			database.createObjectStore(STORE_NAME, KeyPath("id"))
		}
	}

	override val storeName: String get() = STORE_NAME

	@OptIn(ExperimentalWasmJsInterop::class)
	override fun toJs(entity: EncryptedBudgetElement): JsAny = entity.toJs()

	@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
	@OptIn(ExperimentalWasmJsInterop::class)
	override fun toKt(dbEntity: JsAny): CachedBudgetElement = (dbEntity as JsBudgetElement).toKt()

	override fun keyOf(entity: EncryptedBudgetElement): Key = Key(entity.id)

}