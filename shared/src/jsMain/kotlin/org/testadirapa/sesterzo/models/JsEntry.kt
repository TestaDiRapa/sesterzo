package org.testadirapa.sesterzo.models

import org.testadirapa.sesterzo.model.EncryptedEntry
import org.testadirapa.sesterzo.utils.emptyObject

external interface JsEntry {
	var id: String
	var updated: Double
	var deleted: Boolean
	var budgetCoordinates: String
	var encryptedSelf: String?
}

fun EncryptedEntry.toJs(): JsEntry {
	val js = emptyObject<JsEntry>()
	js.id = id
	js.updated = updated.toDouble()
	js.deleted = deleted
	js.budgetCoordinates = "$spaceId-$budgetId"
	js.encryptedSelf = encryptedSelf
	return js
}

fun JsEntry.toKt(): EncryptedEntry = EncryptedEntry(
	id = id,
	updated = updated.toLong(),
	deleted = deleted,
	budgetId = budgetCoordinates.split("-", limit = 2).last(),
	transientSpaceId = budgetCoordinates.split("-", limit = 2).first(),
	encryptedSelf = encryptedSelf
)