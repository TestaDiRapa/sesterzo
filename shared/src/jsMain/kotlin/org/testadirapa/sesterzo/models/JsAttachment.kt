package org.testadirapa.sesterzo.models

import org.testadirapa.sesterzo.cache.model.CachedAttachment
import org.testadirapa.sesterzo.model.EncryptedAttachment
import org.testadirapa.sesterzo.utils.currentTimeMillis
import org.testadirapa.sesterzo.utils.emptyObject

external interface JsAttachment {
	var id: String
	var spaceId: String
	var encryptedSelf: String?
	var insertedAt: Double
}

fun EncryptedAttachment.toJs(insertedAt: Double = currentTimeMillis()): JsAttachment {
	val js = emptyObject<JsAttachment>()
	js.id = id
	js.spaceId = spaceId
	js.encryptedSelf = encryptedSelf
	js.insertedAt = insertedAt
	return js
}

fun JsAttachment.toKt(): CachedAttachment = CachedAttachment(
	id = id,
	entity = EncryptedAttachment(
		id = id,
		spaceId = spaceId,
		encryptedSelf = encryptedSelf
	),
	insertedAt = insertedAt.toLong()
)