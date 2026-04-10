package org.testadirapa.sesterzo.config

import com.juul.indexeddb.KeyPath
import com.juul.indexeddb.openDatabase
import org.testadirapa.sesterzo.cache.JsPersistentCache
import org.testadirapa.sesterzo.cache.JsSpacePersistentCache
import org.testadirapa.sesterzo.cache.PersistentCache
import org.testadirapa.sesterzo.storage.LocalStorageJsFacade
import org.testadirapa.sesterzo.storage.StorageFacade

actual object PlatformContext {
	actual suspend fun storageFacade(): StorageFacade = LocalStorageJsFacade()

	private var _persistentCache: JsPersistentCache? = null

	actual suspend fun persistentCache(): PersistentCache {
		if (_persistentCache == null) {
			_persistentCache = JsPersistentCache(
				database = openDatabase("sesterzo.app.db", 1) { database, oldVersion, _ ->
					if (oldVersion < 1) {
						database.createObjectStore(JsSpacePersistentCache.STORE_NAME, KeyPath("id"))
					}
				}
			)
		}
		return _persistentCache!!
	}

}