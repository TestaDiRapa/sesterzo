package org.testadirapa.sesterzo.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.model.EncryptedBudget

abstract class BudgetDAO(client: DBClient) : GenericMultiCollectionDAO<EncryptedBudget>(client) {

	override fun getCollection(spaceId: String): MongoCollection<EncryptedBudget> = client.getCollection(spaceId)
}
