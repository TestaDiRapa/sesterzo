package org.testadirapa.sesterzo.cache

import com.juul.indexeddb.Database
import com.juul.indexeddb.Key
import com.juul.indexeddb.KeyPath
import com.juul.indexeddb.VersionChangeTransaction
import org.testadirapa.sesterzo.cache.model.CachedUser
import org.testadirapa.sesterzo.model.User
import org.testadirapa.sesterzo.models.JsUser
import org.testadirapa.sesterzo.models.toJs
import org.testadirapa.sesterzo.models.toKt

class JsUserPersistentCache(
	database: Database
) : JsAbstractPersistentCache<User, CachedUser>(database), UserPersistentCache {

	companion object {
		private const val STORE_NAME = "user"

		fun VersionChangeTransaction.initUserStorage(database: Database) = with(this) {
			database.createObjectStore(STORE_NAME, KeyPath("id"))
		}
	}

	override val storeName: String get() = STORE_NAME

	@OptIn(ExperimentalWasmJsInterop::class)
	override fun toJs(entity: User): JsAny = entity.toJs()

	@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
	@OptIn(ExperimentalWasmJsInterop::class)
	override fun toKt(dbEntity: JsAny): CachedUser = (dbEntity as JsUser).toKt()

	override fun keyOf(entity: User): Key = Key(entity.id)

}