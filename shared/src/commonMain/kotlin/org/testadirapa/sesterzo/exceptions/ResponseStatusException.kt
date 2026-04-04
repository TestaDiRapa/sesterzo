package org.testadirapa.sesterzo.exceptions

class ResponseStatusException(
	val msg: String?,
	val label: ExceptionLabel?
) : Exception()