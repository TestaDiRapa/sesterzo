package org.testadirapa.sesterzo.validators

class CompositeValidator<T>(
	vararg validators: Validator<T>,
) : Validator<T> {
	private val validators = validators.toList()
	override fun isValid(value: T): Boolean = validators.all { it.isValid(value) }
}