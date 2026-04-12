package org.testadirapa.sesterzo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import org.testadirapa.sesterzo.serialization.Serialization

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
) : BudgetElement, DecryptedData<EncryptedBudgetElement> {

	@SerialName("_id")
	override val id: String = "$budgetElementId-$version"

	override fun getJsonToEncrypt(): JsonObject = JsonObject(
		mapOf(
			DecryptedBudgetElement::elements.name to Serialization.json.encodeToJsonElement(elements)
		)
	)

	override fun toEncryptedEntity(
		encryptedSelf: Base64String?
	): EncryptedBudgetElement = EncryptedBudgetElement(
		budgetElementId = budgetElementId,
		version = version,
		spaceId = spaceId,
		type = type,
		encryptedSelf = encryptedSelf
	)
}

@Serializable
data class EncryptedBudgetElement(
	override val budgetElementId: String,
	override val version: Int,
	override val spaceId: String,
	override val type: BudgetElement.BudgetElementType,
	override val encryptedSelf: Base64String?,
) : BudgetElement, EncryptedData<DecryptedBudgetElement> {

	@SerialName("_id")
	override val id: String = "$budgetElementId-$version"

	override fun toDecryptedData(decryptedFields: JsonObject): DecryptedBudgetElement = DecryptedBudgetElement(
		budgetElementId = budgetElementId,
		version = version,
		spaceId = spaceId,
		type = type,
		elements = decryptedFields[DecryptedBudgetElement::elements.name]?.jsonObject?.mapValues {
			it.value.jsonPrimitive.long
		} ?: emptyMap()
	)
}