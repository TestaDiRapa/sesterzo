package org.testadirapa.sesterzo.exceptions

import kotlinx.serialization.Serializable

@Serializable
class ResponseStatusException(
	val msg: String?,
	val status: Int,
	val url: String,
	val method: String,
	override val label: ExceptionLabel?
) : Exception(), ExceptionWithLabel