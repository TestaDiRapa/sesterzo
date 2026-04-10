package org.testadirapa.sesterzo.cache

import com.juul.indexeddb.Database

class JsPersistentCache(
	database: Database,
) : PersistentCache {

	override val space: SpacePersistentCache by lazy { JsSpacePersistentCache(database) }

}