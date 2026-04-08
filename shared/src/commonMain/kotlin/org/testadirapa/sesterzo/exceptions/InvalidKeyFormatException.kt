package org.testadirapa.sesterzo.exceptions

class InvalidKeyFormatException : Exception(), ExceptionWithLabel {
	override val label: ExceptionLabel = ExceptionLabel.InvalidKeyFormat
}