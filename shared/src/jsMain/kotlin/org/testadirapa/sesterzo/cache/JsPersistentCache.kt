package org.testadirapa.sesterzo.cache

import com.juul.indexeddb.Database
import com.juul.indexeddb.VersionChangeTransaction
import org.testadirapa.sesterzo.cache.JsAttachmentPersistentCache.Companion.initAttachmentStorage
import org.testadirapa.sesterzo.cache.JsBudgetElementPersistentCache.Companion.initBudgetElementStorage
import org.testadirapa.sesterzo.cache.JsBudgetPersistentCache.Companion.initBudgetStorage
import org.testadirapa.sesterzo.cache.JsEntryPersistentCache.Companion.initExpenseStorage
import org.testadirapa.sesterzo.cache.JsSpacePersistentCache.Companion.initSpaceStorage
import org.testadirapa.sesterzo.cache.JsUserPersistentCache.Companion.initUserStorage

class JsPersistentCache(
	database: Database,
) : PersistentCache {

	companion object {
		fun VersionChangeTransaction.initStorage(database: Database) {
			initAttachmentStorage(database)
			initBudgetStorage(database)
			initBudgetElementStorage(database)
			initExpenseStorage(database)
			initSpaceStorage(database)
			initUserStorage(database)
		}
	}

	override val attachment: AttachmentPersistentCache by lazy { JsAttachmentPersistentCache(database) }
	override val budget: BudgetPersistentCache by lazy { JsBudgetPersistentCache(database) }
	override val budgetElement: BudgetElementPersistentCache by lazy { JsBudgetElementPersistentCache(database) }
	override val entry: EntryPersistentCache by lazy { JsEntryPersistentCache(database) }
	override val space: SpacePersistentCache by lazy { JsSpacePersistentCache(database) }
	override val user: UserPersistentCache by lazy { JsUserPersistentCache(database) }
}