package org.testadirapa.sesterzo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

@Serializable
sealed interface Attachment : Identifiable, SpaceData

@Serializable
data class DecryptedAttachment(
	@SerialName("_id") override val id: String,
	override val spaceId: String,
	val data: Base64String
) : Attachment, DecryptedData<EncryptedAttachment> {

	override fun getJsonToEncrypt(): JsonObject = JsonObject(
		mapOf(
			DecryptedAttachment::data.name to JsonPrimitive(data),
		)
	)

	override fun toEncryptedEntity(encryptedSelf: Base64String?): EncryptedAttachment = EncryptedAttachment(
		id = id,
		spaceId = spaceId,
		encryptedSelf = encryptedSelf
	)

}

@Serializable
data class EncryptedAttachment(
	@SerialName("_id") override val id: String,
	override val spaceId: String,
	override val encryptedSelf: Base64String?,
) : Attachment, EncryptedData<DecryptedAttachment> {

	override fun toDecryptedData(decryptedFields: JsonObject): DecryptedAttachment = DecryptedAttachment(
		id = id,
		spaceId = spaceId,
		data = decryptedFields.getValue(DecryptedAttachment::data.name).jsonPrimitive.content
	)


}
