package org.testadirapa.sesterzo.model

sealed interface FixedExpenses : Versionable

data class DecryptedFixedExpenses(
	override val id: String,
	override val version: SemanticVersion,
	val fixedExpenses: Map<String, Amount>
) : FixedExpenses, DecryptedData

data class EncryptedFixedExpenses(
	override val id: String,
	override val version: SemanticVersion,
	override val encryptedSelf: Base64String
) : FixedExpenses, EncryptedData