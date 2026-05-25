package org.testadirapa.sesterzo.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.EncryptedBudget
import org.testadirapa.sesterzo.model.VersionableReference
import org.testadirapa.sesterzo.model.dto.BulkOperationElementResult

abstract class BudgetDAO(client: DBClient) : GenericMultiCollectionDAO<EncryptedBudget>(client) {

	override fun getCollection(spaceId: String): MongoCollection<EncryptedBudget> = client.getCollection(spaceId)
	abstract fun getBudgetsForYear(spaceId: String, year: Int): Flow<EncryptedBudget>
	abstract suspend fun getFirstBudgetAfter(spaceId: String, year: Int, month: Int): EncryptedBudget?
	abstract suspend fun getFirstBudgetBefore(spaceId: String, year: Int, month: Int): EncryptedBudget?

	/**
	 * Updates the templateReference in [fieldName] to the version passed in [budgetElementReference], returning a flow
	 * of [BulkOperationElementResult] for the budgets that are successive to [budgetId] (including it if [inclusiveStart]
	 * is true) where the current reference for that template is less or equal than [budgetElementReference].
	 * This operation will create a new version of each budget successfully update.
	 *
	 * There are some caveats on the safety in case of concurrent operations:
	 * - Multiple updates with the same [budgetElementReference] will lead to multiple increases of [EncryptedBudget.version] -> not ideal but ok.
	 * - If someone calls [getBudgetsForYear] or another bulk read operation while the update is ongoing,
	 * it will receive an inconsistent state of the documents.
	 */
	abstract fun updateTemplatesVersionOnBudgets(
		spaceId: String,
		budgetId: String,
		inclusiveStart: Boolean,
		fieldName: String,
		updatedField: EncryptedBudget.() -> VersionableReference,
		budgetElementReference: VersionableReference
	): Flow<BulkOperationElementResult<EncryptedBudget>>

	abstract suspend fun setEncryptedSelfOnBudget(
		spaceId: String,
		budgetId: String,
		budgetVersion: Int,
		encryptedSelf: Base64String?
	): EncryptedBudget?
}
