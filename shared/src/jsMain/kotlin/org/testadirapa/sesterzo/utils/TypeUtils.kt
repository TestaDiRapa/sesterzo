package org.testadirapa.sesterzo.utils

import org.testadirapa.sesterzo.models.Record

@OptIn(ExperimentalWasmJsInterop::class)
fun <T : JsAny> emptyObject(): T = js("({})") as T

fun currentTimeMillis(): Double = js("Date.now()") as Double

fun <V, V_JS> mapToObject(
	map: Map<String, V>,
	convertValue: (value: V) -> V_JS,
): Record<String, V_JS> {
	val obj = js("{}")
	for ((key, value) in map) {
		obj[key] = convertValue(value)
	}
	return obj
}

fun <V, V_JS> objectToMap(
	obj: Record<String, V_JS>,
	convertValue: (value: V_JS) -> V,
): Map<String, V> {
	val map = mutableMapOf<String, V>()
	val entries = js("Object.entries(obj)") as Array<Array<dynamic>>
	for (keyValue in entries) {
		val value = keyValue[1]
		if (value !== undefined) {
			map[keyValue[0] as String] = convertValue(value)
		}
	}
	return map
}