package org.testadirapa.sesterzo.model

enum class Currency(val symbol: String, val writer: (Amount) -> String) {
	EUR("€", { "€ ${it / 100.0}" }),
	USD("$", { "$ ${it / 100.0}" }),
	JPY("¥", { "¥ $it" }),
	GBP("£", { "£ ${it / 100.0}" }),
	CNY("¥", { "¥ ${it / 100.0}" }),
}