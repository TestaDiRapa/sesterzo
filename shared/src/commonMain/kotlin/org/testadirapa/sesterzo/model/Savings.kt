package org.testadirapa.sesterzo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface Savings : SpaceData, Versionable {
	val savingsId: String
}

@Serializable
data class DecryptedSavings(
	override val savingsId: String,
	override val version: Int,
	override val spaceId: String,
	val savings: Map<String, Amount> = emptyMap()
) : Savings, DecryptedData {

	@SerialName("_id")
	override val id: String = "$savingsId-$version"

}

@Serializable
data class EncryptedSavings(
	override val savingsId: String,
	override val version: Int,
	override val spaceId: String,
	override val encryptedSelf: Base64String,
) : Savings, EncryptedData {

	@SerialName("_id")
	override val id: String = "$savingsId-$version"

}
