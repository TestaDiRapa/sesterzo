package org.testadirapa.sesterzo.exceptions

class ResponseStatusException(
	val msg: String?,
	override val label: ExceptionLabel?
) : Exception(), ExceptionWithLabel