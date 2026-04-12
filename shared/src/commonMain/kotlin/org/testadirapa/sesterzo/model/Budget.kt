package org.testadirapa.sesterzo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import org.testadirapa.sesterzo.serialization.Serialization
import kotlin.collections.mapValues

@Serializable
sealed interface Budget : SpaceData, Versionable {
	val year: Int
	val month: Int
	val expensesReference: VersionableReference
	val incomeReference: VersionableReference
	val savingsReference: VersionableReference
}

@Serializable
data class DecryptedBudget(
	override val version: Int,
	override val spaceId: String,
	override val year: Int,
	override val month: Int,
	override val expensesReference: VersionableReference,
	override val incomeReference: VersionableReference,
	override val savingsReference: VersionableReference,
	val fixedExpenses: Map<String, Amount> = emptyMap(),
	val income: Map<String, Amount> = emptyMap(),
	val savings: Map<String, Amount> = emptyMap(),
) : Budget, DecryptedData<EncryptedBudget> {

	@SerialName("_id")
	override val id: String
		get() = "budget-${year}-${month}"

	override fun getJsonToEncrypt(): JsonObject = JsonObject(
		mapOf(
			DecryptedBudget::fixedExpenses.name to Serialization.json.encodeToJsonElement(fixedExpenses),
			DecryptedBudget::income.name to Serialization.json.encodeToJsonElement(income),
			DecryptedBudget::savings.name to Serialization.json.encodeToJsonElement(savings),
		)
	)

	override fun toEncryptedEntity(encryptedSelf: Base64String?): EncryptedBudget = EncryptedBudget(
		version = version,
		spaceId = spaceId,
		year = year,
		month = month,
		expensesReference = expensesReference,
		incomeReference = incomeReference,
		savingsReference = savingsReference,
		encryptedSelf = encryptedSelf
	)
}

@Serializable
data class EncryptedBudget(
	override val version: Int,
	override val spaceId: String,
	override val year: Int,
	override val month: Int,
	override val expensesReference: VersionableReference,
	override val incomeReference: VersionableReference,
	override val savingsReference: VersionableReference,
	override val encryptedSelf: Base64String?,
) : Budget, EncryptedData<DecryptedBudget> {

	@SerialName("_id")
	override val id: String
		get() = "budget-${year}-${month}"

	override fun toDecryptedData(decryptedFields: JsonObject): DecryptedBudget = DecryptedBudget(
		version = version,
		spaceId = spaceId,
		year = year,
		month = month,
		expensesReference = expensesReference,
		incomeReference = incomeReference,
		savingsReference = savingsReference,
		fixedExpenses = decryptedFields[DecryptedBudget::fixedExpenses.name]?.jsonObject?.mapValues {
			it.value.jsonPrimitive.long
		} ?: emptyMap(),
		income = decryptedFields[DecryptedBudget::income.name]?.jsonObject?.mapValues {
			it.value.jsonPrimitive.long
		} ?: emptyMap(),
		savings = decryptedFields[DecryptedBudget::savings.name]?.jsonObject?.mapValues {
			it.value.jsonPrimitive.long
		} ?: emptyMap(),
	)

}