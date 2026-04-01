package org.testadirapa.sesterzo.models

import org.testadirapa.sesterzo.validators.Validator

data class FormValue<T>(
	val value: T? = null,
	private val validator: Validator<T>? = null
) {
	val isValid: Boolean
		get() = value == null || (validator?.isValid(value) != false)
	val displayError: Boolean
		get() = value != null && !isValid

	fun update(newValue: T): FormValue<T> = copy(value = newValue)
}