package org.testadirapa.sesterzo.exceptions

class InvalidPrivateKeyException : Exception(), ExceptionWithLabel {
	override val label: ExceptionLabel = ExceptionLabel.InvalidPrivateKey
}