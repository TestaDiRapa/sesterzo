package org.testadirapa.sesterzo.validators

import org.testadirapa.sesterzo.model.Bip39RecoveryKey.Companion.isBip39Valid

object Bip39Validator : Validator<String> {

	private val charactersRegex = Regex("^[a-z\\s]+$")

	override fun isValid(value: String): Boolean =
		value.isNotEmpty() &&
			value.lowercase().matches(charactersRegex) &&
			value.isBip39Valid()
}