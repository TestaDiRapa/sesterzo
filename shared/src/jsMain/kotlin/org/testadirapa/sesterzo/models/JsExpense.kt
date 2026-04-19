package org.testadirapa.sesterzo.models

import org.testadirapa.sesterzo.model.EncryptedExpense
import org.testadirapa.sesterzo.utils.emptyObject

external interface JsExpense {
	var id: String
	var updated: Double
	var deleted: Boolean
	var budgetCoordinates: String
	var encryptedSelf: String?
}

fun EncryptedExpense.toJs(): JsExpense {
	val js = emptyObject<JsExpense>()
	js.id = id
	js.updated = updated.toDouble()
	js.deleted = deleted
	js.budgetCoordinates = "$spaceId-$budgetId"
	js.encryptedSelf = encryptedSelf
	return js
}

fun JsExpense.toKt(): EncryptedExpense = EncryptedExpense(
	id = id,
	updated = updated.toLong(),
	deleted = deleted,
	budgetId = budgetCoordinates.split("-", limit = 2).last(),
	transientSpaceId = budgetCoordinates.split("-", limit = 2).first(),
	encryptedSelf = encryptedSelf
)