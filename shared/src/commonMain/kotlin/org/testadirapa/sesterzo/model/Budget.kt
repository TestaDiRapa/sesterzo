package org.testadirapa.sesterzo.model

sealed interface Budget : Identifiable {
	val fixedExpensesReference: VersionableReference
	val incomeReference: VersionableReference
	val savingsReference: VersionableReference
}

data class DecryptedBudget(
	override val id: String,
	override val fixedExpensesReference: VersionableReference,
	override val incomeReference: VersionableReference,
	override val savingsReference: VersionableReference,
	val expenses: Map<ExpenseKey, Amount>
) : Budget, DecryptedData {

	data class ExpenseKey(val timestamp: Timestamp, val key: String)

}

data class EncryptedBudget(
	override val id: String,
	override val fixedExpensesReference: VersionableReference,
	override val incomeReference: VersionableReference,
	override val savingsReference: VersionableReference,
	override val encryptedSelf: Base64String
) : Budget, EncryptedData