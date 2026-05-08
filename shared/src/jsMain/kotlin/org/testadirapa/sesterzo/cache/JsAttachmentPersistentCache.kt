package org.testadirapa.sesterzo.cache

import com.juul.indexeddb.Database
import com.juul.indexeddb.Key
import com.juul.indexeddb.KeyPath
import com.juul.indexeddb.VersionChangeTransaction
import org.testadirapa.sesterzo.cache.model.CachedAttachment
import org.testadirapa.sesterzo.model.EncryptedAttachment
import org.testadirapa.sesterzo.models.JsAttachment
import org.testadirapa.sesterzo.models.toJs
import org.testadirapa.sesterzo.models.toKt

class JsAttachmentPersistentCache(
	database: Database
) : JsAbstractPersistentCache<EncryptedAttachment, CachedAttachment>(database), AttachmentPersistentCache {

	companion object {
		private const val STORE_NAME = "attachment"

		fun VersionChangeTransaction.initAttachmentStorage(database: Database) = with(this) {
			database.createObjectStore(STORE_NAME, KeyPath("id"))
		}
	}

	override val storeName: String get() = STORE_NAME

	@OptIn(ExperimentalWasmJsInterop::class)
	override fun toJs(entity: EncryptedAttachment): JsAny = entity.toJs()

	@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
	@OptIn(ExperimentalWasmJsInterop::class)
	override fun toKt(dbEntity: JsAny): CachedAttachment = (dbEntity as JsAttachment).toKt()

	override fun keyOf(entity: EncryptedAttachment): Key = Key(entity.id)

}