package org.testadirapa.sesterzo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface BudgetElement : Versionable, SpaceData {
	val budgetElementId: String
	val type: BudgetElementType

	enum class BudgetElementType { FixedExpenses, Income, Savings }
}

@Serializable
data class DecryptedBudgetElement(
	override val budgetElementId: String,
	override val version: Int,
	override val spaceId: String,
	override val type: BudgetElement.BudgetElementType,
	val elements: Map<String, Amount> = emptyMap()
) : BudgetElement, DecryptedData {

	@SerialName("_id")
	override val id: String = "$budgetElementId-$version"

}

@Serializable
data class EncryptedBudgetElement(
	override val budgetElementId: String,
	override val version: Int,
	override val spaceId: String,
	override val type: BudgetElement.BudgetElementType,
	override val encryptedSelf: Base64String?,
) : BudgetElement, EncryptedData {

	@SerialName("_id")
	override val id: String = "$budgetElementId-$version"

}