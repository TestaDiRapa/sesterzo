package org.testadirapa.sesterzo.validators

object EmailValidator : Validator<String> {
	override fun isValid(value: String): Boolean =
		value.isNotBlank() && value.contains("@") && value.contains(".")
}