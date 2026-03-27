package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

class ResponseStatusException(
	val status: HttpStatusCode,
	val msg: String?,
	val label: ExceptionLabel?
) : Exception()