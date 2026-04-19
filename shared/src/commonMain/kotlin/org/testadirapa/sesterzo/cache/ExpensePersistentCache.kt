package org.testadirapa.sesterzo.cache

import org.testadirapa.sesterzo.model.EncryptedExpense

interface ExpensePersistentCache : PersistenceOperator<EncryptedExpense, EncryptedExpense> {
	suspend fun getAllForBudgetInSpace(spaceId: String, budgetId: String): List<EncryptedExpense>
}