package org.testadirapa.sesterzo.exceptions

class MissingSpaceKeyException(spaceId: String) : Exception("Cannot get key for space: $spaceId"), ExceptionWithLabel {
	override val label: ExceptionLabel = ExceptionLabel.MissignSpaceKey
}