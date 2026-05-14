package org.testadirapa.sesterzo.validators

interface Validator<T> {
	fun isValid(value: T?): Boolean
}