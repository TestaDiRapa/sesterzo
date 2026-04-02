package org.testadirapa.sesterzo.validators

object NotBlankValidator : Validator<String> {
	override fun isValid(value: String): Boolean =
		value.isNotBlank()
}