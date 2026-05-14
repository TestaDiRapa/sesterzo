package org.testadirapa.sesterzo.validators

class MaxLengthOrNullValidator(private val length: Int) : Validator<String?> {
	override fun isValid(value: String?): Boolean = value == null || value.length <= length
}