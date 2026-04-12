package org.testadirapa.sesterzo.dao.impl

import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.dao.BudgetDAO

class BudgetDAOImpl(
	client: DBClient,
) : BudgetDAO(client) {

	override suspend fun initIndexes() {}
}