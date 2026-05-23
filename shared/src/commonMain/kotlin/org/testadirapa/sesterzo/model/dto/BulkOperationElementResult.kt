package org.testadirapa.sesterzo.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class BulkOperationElementResult<T>(
	val element: T,
	val success: Boolean,
)