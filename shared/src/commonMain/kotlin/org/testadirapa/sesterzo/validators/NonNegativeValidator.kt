package org.testadirapa.sesterzo.validators

import org.testadirapa.sesterzo.model.Amount

object NonNegativeValidator : Validator<Amount> {
	override fun isValid(value: Amount): Boolean = value >= 0
}