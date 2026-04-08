package org.testadirapa.sesterzo.exceptions

class CannotDecryptWithRsaKeyException : Exception(), ExceptionWithLabel {
	override val label: ExceptionLabel = ExceptionLabel.CannotDecryptWithRsaKey
}