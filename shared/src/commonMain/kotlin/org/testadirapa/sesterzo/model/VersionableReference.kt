package org.testadirapa.sesterzo.model

import kotlinx.serialization.Serializable

@Serializable
data class VersionableReference(
	val id: String,
	val version: Int
) {
	fun toId(): String = Versionable.idOf(entityId = id, version = version)
}