package org.testadirapa.sesterzo.cache

import com.juul.indexeddb.Database
import com.juul.indexeddb.Key
import com.juul.indexeddb.KeyPath
import com.juul.indexeddb.VersionChangeTransaction
import org.testadirapa.sesterzo.cache.model.CachedSpace
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.models.JsSpace
import org.testadirapa.sesterzo.models.toJs
import org.testadirapa.sesterzo.models.toKt

class JsSpacePersistentCache(
	database: Database
) : JsAbstractPersistentCache<Space, CachedSpace>(database), SpacePersistentCache {

	companion object {
		private const val STORE_NAME = "space"

		fun VersionChangeTransaction.initSpaceStorage(database: Database) = with(this) {
			database.createObjectStore(STORE_NAME, KeyPath("id"))
		}
	}

	override val storeName: String get() = STORE_NAME

	@OptIn(ExperimentalWasmJsInterop::class)
	override fun toJs(entity: Space): JsAny = entity.toJs()

	@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
	@OptIn(ExperimentalWasmJsInterop::class)
	override fun toKt(dbEntity: JsAny): CachedSpace = (dbEntity as JsSpace).toKt()

	override fun keyOf(entity: Space): Key = Key(entity.id)

}