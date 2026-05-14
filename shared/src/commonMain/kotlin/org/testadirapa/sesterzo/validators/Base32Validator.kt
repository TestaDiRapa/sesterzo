package org.testadirapa.sesterzo.validators

object Base32Validator : Validator<String> {

	private val base32Regex = Regex("^[A-Z2-7]+=*$")

	override fun isValid(value: String?): Boolean =
		!value.isNullOrBlank() && base32Regex.matches(value)
}