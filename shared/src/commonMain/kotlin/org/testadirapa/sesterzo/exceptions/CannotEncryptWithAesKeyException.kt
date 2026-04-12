package org.testadirapa.sesterzo.exceptions

class CannotEncryptWithAesKeyException : Exception(), ExceptionWithLabel {
	override val label: ExceptionLabel = ExceptionLabel.CannotEncryptWithAesKey
}