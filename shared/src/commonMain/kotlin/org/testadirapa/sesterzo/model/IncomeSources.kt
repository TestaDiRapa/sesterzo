package org.testadirapa.sesterzo.model

sealed interface IncomeSources : Versionable

data class DecryptedIncomeSources(
	override val id: String,
	override val version: SemanticVersion,
	val sources: Map<String, Amount>
) : IncomeSources, DecryptedData

data class EncryptedIncomeSources(
	override val id: String,
	override val version: SemanticVersion,
	override val encryptedSelf: Base64String
) : IncomeSources, EncryptedData
