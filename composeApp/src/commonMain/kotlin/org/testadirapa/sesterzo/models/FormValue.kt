package org.testadirapa.sesterzo.models

import org.testadirapa.sesterzo.validators.Validator

sealed class Optional<out T> {
	data object Absent : Optional<Nothing>()
	data class Present<T>(val value: T) : Optional<T>()

	val orNull: T? get() = (this as? Present)?.value
}

data class FormValue<T>(
	val value: Optional<T> = Optional.Absent,
	private val validator: Validator<T>? = null
) {

	val isValid: Boolean
		get() = validator == null ||
			(value is Optional.Present<T> && validator.isValid(value.value))
	val displayError: Boolean
		get() = value is Optional.Present<T> && !isValid
	val validValue: T
		get() = if (value is Optional.Present<T> && isValid) {
			value.value
		} else {
			throw IllegalStateException("Value is invalid")
		}

	fun update(newValue: T): FormValue<T> = copy(
		value = Optional.Present(newValue)
	)
}