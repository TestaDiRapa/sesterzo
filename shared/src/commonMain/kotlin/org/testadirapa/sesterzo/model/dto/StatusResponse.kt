package org.testadirapa.sesterzo.model.dto

import kotlinx.serialization.Serializable
import org.testadirapa.sesterzo.exceptions.ExceptionLabel

@Serializable
data class StatusResponse(
	val message: String? = null,
	val code: Int? = null,
	val label: ExceptionLabel? = null
)