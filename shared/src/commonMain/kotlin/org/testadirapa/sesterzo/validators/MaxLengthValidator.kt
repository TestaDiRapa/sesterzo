package org.testadirapa.sesterzo.validators

class MaxLengthValidator(private val length: Int) : Validator<String> {
	override fun isValid(value: String?): Boolean = !value.isNullOrBlank() && value.length <= length
}