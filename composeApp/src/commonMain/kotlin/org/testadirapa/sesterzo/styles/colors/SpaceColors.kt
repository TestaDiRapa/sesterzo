package org.testadirapa.sesterzo.styles.colors

import androidx.compose.ui.graphics.Color
import org.testadirapa.sesterzo.model.Space

enum class SpaceColor(val color: Color) {
	Amber(Color(0xFFE8B547)),
	Coral(Color(0xFFE89B7C)),
	Rose(Color(0xFFEE9AB0)),
	Lilac(Color(0xFFC3A8E8)),
	Indigo(Color(0xFF9FB0EE)),
	Sky(Color(0xFF8FCFE8)),
	Teal(Color(0xFF7FCBC0)),
	Mint(Color(0xFF9BD1B8)),
	Olive(Color(0xFFC5CE7E)),
	Sand(Color(0xFFD6C2A0))
}

val onSpaceColor = Color(0xFF1A1200)

fun randomSpaceColor(): SpaceColor = SpaceColor.entries.random()

fun Space.colorOrDefault(): SpaceColor =
	if (color != null) {
		SpaceColor.entries.firstOrNull {
			it.color.value == color
		} ?: SpaceColor.Amber
	} else {
		SpaceColor.Amber
	}