package org.testadirapa.sesterzo.models

import org.testadirapa.sesterzo.cache.model.CachedSpace
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.utils.currentTimeMillis
import org.testadirapa.sesterzo.utils.emptyObject
import org.testadirapa.sesterzo.utils.mapToObject
import org.testadirapa.sesterzo.utils.objectToMap

@OptIn(ExperimentalWasmJsInterop::class)
external interface JsSpace : JsAny {
	var id: String
	var version: Int
	var name: String
	var owner: String
	var fixedExpensesTemplateId: String
	var incomeSourcesTemplateId: String
	var savingsTemplateId: String
	var users: Record<String, JsAccessKey>
	var pictureReference: String?
	var color: JsRGBColor?
	var insertedAt: Double
}

fun Space.toJs(insertedAt: Double = currentTimeMillis()): JsSpace {
	val js = emptyObject<JsSpace>()
	js.id = id
	js.version = version
	js.name = name
	js.owner = owner
	js.fixedExpensesTemplateId = fixedExpensesTemplateId
	js.incomeSourcesTemplateId = incomeSourcesTemplateId
	js.savingsTemplateId = savingsTemplateId
	js.users = mapToObject(users) { it.toJs() }
	js.pictureReference = pictureReference
	js.color = color?.toJs()
	js.insertedAt = insertedAt
	return js
}

fun JsSpace.toKt(): CachedSpace = CachedSpace(
	id = id,
	entity = Space(
		id = id,
		version = version,
		name = name,
		owner = owner,
		fixedExpensesTemplateId = fixedExpensesTemplateId,
		incomeSourcesTemplateId = incomeSourcesTemplateId,
		savingsTemplateId = savingsTemplateId,
		users = objectToMap(users) { it.toKt() },
		color = color?.toKt(),
		pictureReference = pictureReference
	),
	insertedAt = insertedAt.toLong()
)