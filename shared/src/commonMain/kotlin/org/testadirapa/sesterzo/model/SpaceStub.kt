package org.testadirapa.sesterzo.model

import kotlinx.serialization.Serializable

@Serializable
data class SpaceStub(
	val id: String,
	val name: String,
	val users: Map<String, AccessKey>
)
