package org.testadirapa.sesterzo.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.model.EncryptedAttachment
import org.testadirapa.sesterzo.model.EncryptedBudget

abstract class AttachmentDAO(client: DBClient) : GenericMultiCollectionDAO<EncryptedAttachment>(client) {

	override fun getCollection(spaceId: String): MongoCollection<EncryptedAttachment> = client.getCollection(spaceId)

}
