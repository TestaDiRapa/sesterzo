package org.testadirapa.sesterzo.validators

object EmailValidator : Validator<String> {
	override fun isValid(value: String?): Boolean =
		!value.isNullOrBlank() && value.contains("@") && value.contains(".")
}