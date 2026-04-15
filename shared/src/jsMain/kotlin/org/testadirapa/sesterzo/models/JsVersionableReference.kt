package org.testadirapa.sesterzo.models

import org.testadirapa.sesterzo.model.VersionableReference
import org.testadirapa.sesterzo.utils.emptyObject

external interface JsVersionableReference {
	var id: String
	var version: Int
}

fun VersionableReference.toJs(): JsVersionableReference {
	val js = emptyObject<JsVersionableReference>()
	js.id = id
	js.version = version
	return js
}

fun JsVersionableReference.toKt(): VersionableReference = VersionableReference(
	id = id,
	version = version
)