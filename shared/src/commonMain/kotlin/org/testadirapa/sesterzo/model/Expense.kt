package org.testadirapa.sesterzo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

@Serializable
sealed interface Expense : Identifiable {
	val updated: Timestamp
	val deleted: Boolean
	val budgetId: String
}

@Serializable
data class DecryptedExpense(
	@SerialName("_id") override val id: String,
	override val updated: Timestamp,
	override val deleted: Boolean,
	override val budgetId: String,
	val label: String,
	val amount: Amount,
) : Expense, DecryptedData<EncryptedExpense> {

	override fun getJsonToEncrypt(): JsonObject = JsonObject(
		mapOf(
			DecryptedExpense::label.name to JsonPrimitive(label),
			DecryptedExpense::amount.name to JsonPrimitive(amount),
		)
	)

	override fun toEncryptedEntity(encryptedSelf: Base64String?): EncryptedExpense = EncryptedExpense(
		id = id,
		updated = updated,
		deleted = deleted,
		budgetId = budgetId,
		encryptedSelf = encryptedSelf,
	)

}

@Serializable
data class EncryptedExpense(
	@SerialName("_id") override val id: String,
	override val updated: Timestamp,
	override val deleted: Boolean,
	override val budgetId: String,
	override val encryptedSelf: Base64String?,
	@Transient val spaceId: String? = null,
) : Expense, EncryptedData<DecryptedExpense> {

	override fun toDecryptedData(decryptedFields: JsonObject): DecryptedExpense = DecryptedExpense(
		id = id,
		updated = updated,
		deleted = deleted,
		budgetId = budgetId,
		label = decryptedFields.getValue(DecryptedExpense::label.name).jsonPrimitive.content,
		amount = decryptedFields.getValue(DecryptedExpense::label.name).jsonPrimitive.long,
	)


}
