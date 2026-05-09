package org.testadirapa.sesterzo.model

enum class Currency(
	val symbol: String,
	val writer: (Amount) -> String,
	val formWriter: (Amount) -> String,
	val formReader: (String) -> Amount,
) {
	EUR("€", { "€ ${decimalWriter(it)}" }, decimalWriter, decimalReader),
	USD("$", { "$ ${decimalWriter(it)}" }, decimalWriter, decimalReader),
	JPY("¥", { "¥ $it" }, { "$it" }, simpleReader),
	GBP("£", { "£ ${decimalWriter(it)}" }, decimalWriter, decimalReader),
	CNY("¥", { "¥ ${decimalWriter(it)}" }, decimalWriter, decimalReader),
}

private val decimalWriter: (Amount) -> String = { "${it / 100},${(it % 100).toString().padEnd(2, '0')}" }

private fun String.toLenientLong(): Long =
	replace(Regex("[^0-9]"), "")
		.toLongOrNull() ?: 0

private val decimalReader: (String) -> Amount = { input ->
	val clean = input.trim()
	val negative = clean.startsWith("-")
	val digits = clean.removePrefix("-").replace(",", ".")

	val (intPart, fracPart) = if ('.' in digits) {
		val split = digits.split(".")
		split[0] to split[1].take(2).padEnd(2, '0')
	} else {
		digits to "00"
	}

	val result = (intPart.toLenientLong() * 100L) + fracPart.toLenientLong()
	if (negative) -result else result
}

val simpleReader: (String) -> Amount = { input ->
	val clean = input.trim()
	val negative = clean.startsWith("-")
	val digits = clean.removePrefix("-").toLenientLong()
	if (negative) -digits else digits
}