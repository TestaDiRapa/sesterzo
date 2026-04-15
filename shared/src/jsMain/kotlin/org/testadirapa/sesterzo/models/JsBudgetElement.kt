package org.testadirapa.sesterzo.models

import org.testadirapa.sesterzo.cache.model.CachedBudgetElement
import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.EncryptedBudgetElement
import org.testadirapa.sesterzo.utils.emptyObject

external interface JsBudgetElement {
	var id: String
	var budgetElementId: String
	var version: Int
	var spaceId: String
	var type: String
	var encryptedSelf: String?
	var insertedAt: Double
}

fun EncryptedBudgetElement.toJs(): JsBudgetElement {
	val js = emptyObject<JsBudgetElement>()
	js.id = id
	js.budgetElementId = budgetElementId
	js.version = version
	js.spaceId = spaceId
	js.type = type.name
	js.encryptedSelf = encryptedSelf
	return js
}

fun JsBudgetElement.toKt(): CachedBudgetElement = CachedBudgetElement(
	id = id,
	entity = EncryptedBudgetElement(
		budgetElementId = budgetElementId,
		version = version,
		spaceId = spaceId,
		type = BudgetElement.BudgetElementType.valueOf(type),
		encryptedSelf = encryptedSelf
	),
	insertedAt = insertedAt.toLong()
)