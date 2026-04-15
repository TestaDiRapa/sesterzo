package org.testadirapa.sesterzo.dao.impl

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import kotlinx.coroutines.flow.firstOrNull
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.dao.BudgetElementDAO
import org.testadirapa.sesterzo.model.EncryptedBudgetElement

class BudgetElementDAOImpl(
	client: DBClient,
) : BudgetElementDAO(client) {

	override suspend fun initIndexes(spaceId: String) {
		initIndex(spaceId = spaceId, property = EncryptedBudgetElement::version.name, name = "by_version", unique = false)
	}

	override suspend fun getLatestVersionForId(spaceId: String, budgetElementId: String): EncryptedBudgetElement? =
		getCollection(spaceId)
			.find(Filters.eq(EncryptedBudgetElement::budgetElementId.name, budgetElementId))
			.sort(Sorts.descending(EncryptedBudgetElement::version.name))
			.firstOrNull()

}