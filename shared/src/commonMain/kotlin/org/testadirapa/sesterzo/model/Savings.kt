package org.testadirapa.sesterzo.model

sealed interface Savings : Versionable

data class DecryptedSavings(
	override val id: String,
	override val version: SemanticVersion,
	val savings: Map<String, Amount>
) : Savings, DecryptedData

data class EncryptedSavings(
	override val id: String,
	override val version: SemanticVersion,
	override val encryptedSelf: Base64String
) : Savings, EncryptedData
