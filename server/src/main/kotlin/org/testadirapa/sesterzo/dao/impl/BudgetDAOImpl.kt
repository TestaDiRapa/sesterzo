package org.testadirapa.sesterzo.dao.impl

import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.sun.org.apache.xalan.internal.lib.ExsltDatetime.year
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.dao.BudgetDAO
import org.testadirapa.sesterzo.model.EncryptedBudget
import org.testadirapa.sesterzo.model.VersionableReference
import org.testadirapa.sesterzo.model.dto.BulkOperationElementResult

class BudgetDAOImpl(
	client: DBClient,
) : BudgetDAO(client) {

	companion object {
		private const val BY_YEAR_MONTH = "by_year_month"
	}

	override suspend fun initIndexes(spaceId: String) {
		val collection = getCollection(spaceId)
		if (collection.listIndexes().firstOrNull { it["name"] == BY_YEAR_MONTH } == null) {
			collection.createIndex(
				Indexes.descending(EncryptedBudget::year.name, EncryptedBudget::month.name),
				IndexOptions().name(BY_YEAR_MONTH),
			)
		}
	}

	override fun getBudgetsForYear(spaceId: String, year: Int): Flow<EncryptedBudget> =
		find(spaceId, Filters.eq(EncryptedBudget::year.name, year))

	override suspend fun getFirstBudgetAfter(spaceId: String, year: Int, month: Int): EncryptedBudget? =
		find(
			spaceId = spaceId,
			filter = Filters.or(
				Filters.gt(EncryptedBudget::year.name, year),
				Filters.and(
					Filters.eq(EncryptedBudget::year.name, year),
					Filters.gt(EncryptedBudget::month.name, month)
				)
			)
		).sort(
			Sorts.ascending(EncryptedBudget::year.name, EncryptedBudget::month.name)
		).firstOrNull()

	override suspend fun getFirstBudgetBefore(spaceId: String, year: Int, month: Int): EncryptedBudget? =
		find(
			spaceId = spaceId,
			filter = Filters.or(
				Filters.lt(EncryptedBudget::year.name, year),
				Filters.and(
					Filters.eq(EncryptedBudget::year.name, year),
					Filters.lt(EncryptedBudget::month.name, month)
				)
			)
		).sort(
			Sorts.descending(EncryptedBudget::year.name, EncryptedBudget::month.name)
		).firstOrNull()

	override fun updateTemplatesVersionOnBudgets(
		spaceId: String,
		budgetId: String,
		inclusiveStart: Boolean,
		fieldName: String,
		updatedField: EncryptedBudget.() -> VersionableReference,
		budgetElementReference: VersionableReference
	): Flow<BulkOperationElementResult<EncryptedBudget>> = flow {
		getCollection(spaceId).updateMany(
			filter = Filters.and(
				if (inclusiveStart) {
					Filters.gte("_id", budgetId)
				} else {
					Filters.gt("_id", budgetId)
				},
				Filters.eq("$fieldName.${VersionableReference::id.name}", budgetElementReference.id),
				Filters.lte("$fieldName.${VersionableReference::version.name}", budgetElementReference.version)
			),
			update = Updates.combine(
				Updates.set(fieldName, budgetElementReference),
				Updates.inc(EncryptedBudget::version.name, 1)
			),
		)
		emitAll(
			find(
				spaceId = spaceId,
				filter = if (inclusiveStart) {
					Filters.gte("_id", budgetId)
				} else {
					Filters.gt("_id", budgetId)
				},
			).map {
				BulkOperationElementResult(
					element = it,
					success = it.updatedField().let { ref ->
						ref.id == budgetElementReference.id &&
							ref.version >= budgetElementReference.version // It's ok if some updated the same budget with a more recent version, even concurrently
					},
				)
			}
		)
	}

}