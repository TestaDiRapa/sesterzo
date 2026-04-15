package org.testadirapa.sesterzo.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.model.EncryptedBudgetElement

abstract class BudgetElementDAO(client: DBClient) : GenericMultiCollectionDAO<EncryptedBudgetElement>(client) {

	override fun getCollection(spaceId: String): MongoCollection<EncryptedBudgetElement> = client.getCollection(spaceId)

	abstract suspend fun getLatestVersionForId(spaceId: String, budgetElementId: String): EncryptedBudgetElement?
}
