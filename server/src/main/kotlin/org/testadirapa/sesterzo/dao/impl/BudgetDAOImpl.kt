package org.testadirapa.sesterzo.dao.impl

import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.dao.BudgetDAO
import org.testadirapa.sesterzo.model.EncryptedBudget
import org.testadirapa.sesterzo.model.VersionableReference

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

	override suspend fun updateTemplateVersion(
		spaceId: String,
		budgetId: String,
		budgetVersion: Int,
		fieldName: String,
		budgetElementReference: VersionableReference
	): EncryptedBudget? = getCollection(spaceId).findOneAndUpdate(
		filter = Filters.and(
			Filters.eq("_id", budgetId),
			Filters.eq(EncryptedBudget::version.name, budgetVersion),
		),
		update = Updates.combine(
			Updates.set(fieldName, budgetElementReference),
			Updates.inc(EncryptedBudget::version.name, 1)
		),
		options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
	)

}