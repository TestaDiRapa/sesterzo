package org.testadirapa.sesterzo.cache

import com.juul.indexeddb.Database
import com.juul.indexeddb.VersionChangeTransaction
import org.testadirapa.sesterzo.cache.JsSpacePersistentCache.Companion.initSpaceStorage
import org.testadirapa.sesterzo.cache.JsUserPersistentCache.Companion.initUserStorage

class JsPersistentCache(
	database: Database,
) : PersistentCache {

	companion object {
		fun VersionChangeTransaction.initStorage(database: Database) {
			initSpaceStorage(database)
			initUserStorage(database)
		}
	}

	override val space: SpacePersistentCache by lazy { JsSpacePersistentCache(database) }
	override val user: UserPersistentCache by lazy { JsUserPersistentCache(database) }
}