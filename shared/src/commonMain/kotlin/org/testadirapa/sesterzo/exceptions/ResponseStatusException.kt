package org.testadirapa.sesterzo.exceptions

class ResponseStatusException(
	val msg: String?,
	val status: Int,
	override val label: ExceptionLabel?
) : Exception(), ExceptionWithLabel