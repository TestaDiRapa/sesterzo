package org.testadirapa.sesterzo.cache

import app.cash.sqldelight.db.SqlDriver

class AndroidPersistentCache(
	driver: SqlDriver
) : PersistentCache {

	override val space: SpacePersistentCache by lazy {
		AndroidSpacePersistentCache(driver)
	}
	override val user: UserPersistentCache by lazy {
		AndroidUserPersistentCache(driver)
	}

}