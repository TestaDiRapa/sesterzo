package org.testadirapa.sesterzo.validators

object OttValidator : Validator<String> {

	private val ottRegex = Regex("^[0-9]{6}$")

	override fun isValid(value: String?): Boolean = !value.isNullOrBlank() && ottRegex.matches(value)
}