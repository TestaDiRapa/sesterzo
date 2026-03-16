package org.testadirapa.sesterzo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface IncomeSources : SpaceData, Versionable {
	val incomeId: String
}

@Serializable
data class DecryptedIncomeSources(
	override val incomeId: String,
	override val version: Int,
	override val spaceId: String,
	val sources: Map<String, Amount> = emptyMap()
) : IncomeSources, DecryptedData {

	@SerialName("_id")
	override val id: String = "$incomeId-$version"

}

@Serializable
data class EncryptedIncomeSources(
	override val incomeId: String,
	override val version: Int,
	override val spaceId: String,
	override val encryptedSelf: Base64String,
	override val accessKeys: Set<AccessKey>
) : IncomeSources, EncryptedData {

	@SerialName("_id")
	override val id: String = "$incomeId-$version"

}
