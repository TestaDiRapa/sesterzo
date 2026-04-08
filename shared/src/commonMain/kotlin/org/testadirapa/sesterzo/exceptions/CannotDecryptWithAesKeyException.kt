package org.testadirapa.sesterzo.exceptions

class CannotDecryptWithAesKeyException : Exception(), ExceptionWithLabel {
	override val label: ExceptionLabel = ExceptionLabel.CannotDecryptWithAesKey
}