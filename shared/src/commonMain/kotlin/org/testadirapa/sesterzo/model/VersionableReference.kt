package org.testadirapa.sesterzo.model

data class VersionableReference(
	val id: String,
	val version: SemanticVersion
)