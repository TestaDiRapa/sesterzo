package org.testadirapa.sesterzo.models

import org.testadirapa.sesterzo.model.RGBColor
import org.testadirapa.sesterzo.utils.emptyObject

@OptIn(ExperimentalWasmJsInterop::class)
external interface JsRGBColor : JsAny {
	var red: Int
	var green: Int
	var blue: Int
}

fun RGBColor.toJs(): JsRGBColor {
	val js = emptyObject<JsRGBColor>()
	js.red = red
	js.blue = blue
	js.green = green
	return js
}

fun JsRGBColor.toKt(): RGBColor = RGBColor(
	red = red,
	green = green,
	blue = blue
)