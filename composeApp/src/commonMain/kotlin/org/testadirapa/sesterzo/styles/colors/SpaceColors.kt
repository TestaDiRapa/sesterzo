package org.testadirapa.sesterzo.styles.colors

import androidx.compose.ui.graphics.Color
import org.testadirapa.sesterzo.model.RGBColor
import org.testadirapa.sesterzo.model.Space

enum class SpaceColor(val r: Int, val g: Int, val b: Int) {
	Amber(0xE8, 0xB5, 0x47),
	Coral(0xE8, 0x9B, 0x7C),
	Rose(0xEE, 0x9A, 0xB0),
	Lilac(0xC3, 0xA8, 0xE8),
	Indigo(0x9F, 0xB0, 0xEE),
	Sky(0x8F, 0xCF, 0xE8),
	Teal(0x7F, 0xCB, 0xC0),
	Mint(0x9B, 0xD1, 0xB8),
	Olive(0xC5, 0xCE, 0x7E),
	Sand(0xD6, 0xC2, 0xA0);

	val color: Color get() = Color(red = r, green = g, blue = b)
	val rgbColor: RGBColor get() = RGBColor(red = r, green = g, blue = b)
}

val onSpaceColor = Color(0xFF1A1200)

fun randomSpaceColor(): SpaceColor = SpaceColor.entries.random()

fun Space.colorOrDefault(): SpaceColor =
	if (color != null) {
		SpaceColor.entries.firstOrNull {
			it.r == color?.red && it.r == color?.red && it.b == color?.blue
		} ?: SpaceColor.Amber
	} else {
		SpaceColor.Amber
	}