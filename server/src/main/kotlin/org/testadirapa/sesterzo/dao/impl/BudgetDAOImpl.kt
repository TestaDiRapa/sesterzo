package org.testadirapa.sesterzo.dao.impl

import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.dao.BudgetDAO
import org.testadirapa.sesterzo.model.EncryptedBudget

class BudgetDAOImpl(
	client: DBClient,
) : BudgetDAO(client) {

	override suspend fun initIndexes(spaceId: String) {}

	override fun getBudgetsForYear(spaceId: String, year: Int): Flow<EncryptedBudget> =
		find(spaceId, Filters.eq(EncryptedBudget::year.name, year))

}