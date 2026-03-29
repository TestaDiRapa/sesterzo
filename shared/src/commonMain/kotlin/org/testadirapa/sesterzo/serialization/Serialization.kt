package org.testadirapa.sesterzo.serialization

import kotlinx.serialization.json.Json

object Serialization {

	val json = Json {
		encodeDefaults = false
		explicitNulls = true
		ignoreUnknownKeys = false
	}

	val lenientJson = Json {
		encodeDefaults = false
		explicitNulls = true
		ignoreUnknownKeys = true
	}

}