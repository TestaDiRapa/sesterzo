package org.testadirapa.sesterzo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface FixedExpenses : Versionable, SpaceData {
	val expensesId: String
}

@Serializable
data class DecryptedFixedExpenses(
	override val expensesId: String,
	override val version: Int,
	override val spaceId: String,
	val fixedExpenses: Map<String, Amount> = emptyMap()
) : FixedExpenses, DecryptedData {

	@SerialName("_id")
	override val id: String = "$expensesId-$version"

}

@Serializable
data class EncryptedFixedExpenses(
	override val expensesId: String,
	override val version: Int,
	override val spaceId: String,
	override val encryptedSelf: Base64String,
) : FixedExpenses, EncryptedData {

	@SerialName("_id")
	override val id: String = "$expensesId-$version"

}