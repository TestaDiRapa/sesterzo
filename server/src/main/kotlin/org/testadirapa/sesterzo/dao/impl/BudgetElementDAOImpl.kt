package org.testadirapa.sesterzo.dao.impl

import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.dao.BudgetElementDAO

class BudgetElementDAOImpl(
	client: DBClient,
) : BudgetElementDAO(client) {

	override suspend fun initIndexes() {}
}