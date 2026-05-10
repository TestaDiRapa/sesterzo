package org.testadirapa.sesterzo.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.model.EncryptedBudget
import org.testadirapa.sesterzo.model.VersionableReference

abstract class BudgetDAO(client: DBClient) : GenericMultiCollectionDAO<EncryptedBudget>(client) {

	override fun getCollection(spaceId: String): MongoCollection<EncryptedBudget> = client.getCollection(spaceId)
	abstract fun getBudgetsForYear(spaceId: String, year: Int): Flow<EncryptedBudget>
	abstract suspend fun getFirstBudgetAfter(spaceId: String, year: Int, month: Int): EncryptedBudget?
	abstract suspend fun getFirstBudgetBefore(spaceId: String, year: Int, month: Int): EncryptedBudget?
	abstract suspend fun updateTemplateVersion(
		spaceId: String,
		budgetId: String,
		budgetVersion: Int,
		fieldName: String,
		budgetElementReference: VersionableReference
	): EncryptedBudget?
}
