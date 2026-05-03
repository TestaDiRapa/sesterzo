package org.testadirapa.sesterzo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

@Serializable
sealed interface Entry : Identifiable {
	val updated: Timestamp
	val deleted: Boolean
	val budgetId: String

	enum class EntryType { Expense, Income, Saving }
}

@Serializable
data class DecryptedEntry(
	@SerialName("_id") override val id: String,
	override val updated: Timestamp,
	override val deleted: Boolean,
	override val budgetId: String,
	val createdBy: String,
	val deletedBy: String?,
	val type: Entry.EntryType,
	val label: String,
	val amount: Amount,
) : Entry, DecryptedData<EncryptedEntry> {

	override fun getJsonToEncrypt(): JsonObject = JsonObject(
		mapOf(
			DecryptedEntry::label.name to JsonPrimitive(label),
			DecryptedEntry::amount.name to JsonPrimitive(amount),
			DecryptedEntry::createdBy.name to JsonPrimitive(createdBy),
			DecryptedEntry::deletedBy.name to JsonPrimitive(deletedBy),
			DecryptedEntry::type.name to JsonPrimitive(type.name),
		)
	)

	override fun toEncryptedEntity(encryptedSelf: Base64String?): EncryptedEntry = EncryptedEntry(
		id = id,
		updated = updated,
		deleted = deleted,
		budgetId = budgetId,
		encryptedSelf = encryptedSelf,
	)

}

@Serializable
data class EncryptedEntry(
	@SerialName("_id") override val id: String,
	override val updated: Timestamp,
	override val deleted: Boolean,
	override val budgetId: String,
	override val encryptedSelf: Base64String?,
	@Transient val transientSpaceId: String? = null,
) : Entry, SpaceData, EncryptedData<DecryptedEntry> {

	override val spaceId: String
		get() = checkNotNull(transientSpaceId) { "Expense was not patched with spaceId" }

	override fun toDecryptedData(decryptedFields: JsonObject): DecryptedEntry = DecryptedEntry(
		id = id,
		updated = updated,
		deleted = deleted,
		budgetId = budgetId,
		createdBy = decryptedFields.getValue(DecryptedEntry::createdBy.name).jsonPrimitive.content,
		deletedBy = decryptedFields.getValue(DecryptedEntry::deletedBy.name).jsonPrimitive.contentOrNull,
		type = Entry.EntryType.valueOf(decryptedFields.getValue(DecryptedEntry::type.name).jsonPrimitive.content),
		label = decryptedFields.getValue(DecryptedEntry::label.name).jsonPrimitive.content,
		amount = decryptedFields.getValue(DecryptedEntry::label.name).jsonPrimitive.long,
	)


}
