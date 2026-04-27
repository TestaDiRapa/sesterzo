package org.testadirapa.sesterzo.models

import org.testadirapa.sesterzo.cache.model.CachedBudget
import org.testadirapa.sesterzo.model.EncryptedBudget
import org.testadirapa.sesterzo.utils.currentTimeMillis
import org.testadirapa.sesterzo.utils.emptyObject

@OptIn(ExperimentalWasmJsInterop::class)
external interface JsBudget : JsAny {
	var id: String
	var version: Int
	var spaceId: String
	var year: Int
	var month: Int
	var fuzzyDate: Int
	var expensesReference: JsVersionableReference
	var incomeReference: JsVersionableReference
	var savingsReference: JsVersionableReference
	var encryptedSelf: String?
	var insertedAt: Double
}

fun EncryptedBudget.toJs(insertedAt: Double = currentTimeMillis()): JsBudget {
	val js = emptyObject<JsBudget>()
	js.id = "$spaceId-$id"
	js.version = version
	js.spaceId = spaceId
	js.year = year
	js.month = month
	js.expensesReference = expensesReference.toJs()
	js.incomeReference = incomeReference.toJs()
	js.savingsReference = savingsReference.toJs()
	js.encryptedSelf = encryptedSelf
	js.insertedAt = insertedAt
	return js
}

fun JsBudget.toKt(): CachedBudget = CachedBudget(
	id = id,
	entity = EncryptedBudget(
		version = version,
		spaceId = spaceId,
		year = year,
		month = month,
		expensesReference = expensesReference.toKt(),
		incomeReference = incomeReference.toKt(),
		savingsReference = savingsReference.toKt(),
		encryptedSelf = encryptedSelf
	),
	insertedAt = insertedAt.toLong()
)