package org.testadirapa.sesterzo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Budget : SpaceData, Identifiable {
	val year: Int
	val month: Int
	val expensesReference: VersionableReference
	val incomeReference: VersionableReference
	val savingsReference: VersionableReference
}

@Serializable
data class DecryptedBudget(
	@SerialName("_id") override val id: String,
	override val spaceId: String,
	override val year: Int,
	override val month: Int,
	override val expensesReference: VersionableReference,
	override val incomeReference: VersionableReference,
	override val savingsReference: VersionableReference,
	val fixedExpenses: Map<String, Amount> = emptyMap(),
	val income: Map<String, Amount> = emptyMap(),
	val savings: Map<String, Amount> = emptyMap(),
	val expenses: Map<ExpenseKey, Amount> = emptyMap()
) : Budget, DecryptedData {

	@Serializable
	data class ExpenseKey(val timestamp: Timestamp, val key: String)

}

@Serializable
data class EncryptedBudget(
	@SerialName("_id") override val id: String,
	override val spaceId: String,
	override val year: Int,
	override val month: Int,
	override val expensesReference: VersionableReference,
	override val incomeReference: VersionableReference,
	override val savingsReference: VersionableReference,
	override val encryptedSelf: Base64String,
) : Budget, EncryptedData