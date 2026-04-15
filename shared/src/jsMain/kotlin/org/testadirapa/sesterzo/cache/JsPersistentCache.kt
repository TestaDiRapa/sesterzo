package org.testadirapa.sesterzo.cache

import com.juul.indexeddb.Database
import com.juul.indexeddb.VersionChangeTransaction
import org.testadirapa.sesterzo.cache.JsBudgetElementPersistentCache.Companion.initBudgetElementStorage
import org.testadirapa.sesterzo.cache.JsBudgetPersistentCache.Companion.initBudgetStorage
import org.testadirapa.sesterzo.cache.JsSpacePersistentCache.Companion.initSpaceStorage
import org.testadirapa.sesterzo.cache.JsUserPersistentCache.Companion.initUserStorage

class JsPersistentCache(
	database: Database,
) : PersistentCache {

	companion object {
		fun VersionChangeTransaction.initStorage(database: Database) {
			initBudgetStorage(database)
			initBudgetElementStorage(database)
			initSpaceStorage(database)
			initUserStorage(database)
		}
	}

	override val budget: BudgetPersistentCache by lazy { JsBudgetPersistentCache(database) }
	override val budgetElement: BudgetElementPersistentCache by lazy { JsBudgetElementPersistentCache(database) }
	override val space: SpacePersistentCache by lazy { JsSpacePersistentCache(database) }
	override val user: UserPersistentCache by lazy { JsUserPersistentCache(database) }
}