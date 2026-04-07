package org.testadirapa.sesterzo.validators

object Base64Validator : Validator<String> {

	private val base64Regex = Regex("^[A-Za-z0-9+/]+={0,2}$")

	override fun isValid(value: String): Boolean =
		value.isNotEmpty() && base64Regex.matches(value)
}