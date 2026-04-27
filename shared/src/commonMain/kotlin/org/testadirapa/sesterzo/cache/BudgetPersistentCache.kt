package org.testadirapa.sesterzo.cache

import org.testadirapa.sesterzo.cache.model.CachedBudget
import org.testadirapa.sesterzo.model.EncryptedBudget

interface BudgetPersistentCache : PersistenceOperator<EncryptedBudget, CachedBudget> {
	suspend fun getByYearInSpace(spaceId: String, year: Int): List<CachedBudget>
	suspend fun getFirstBudgetAfter(spaceId: String, year: Int, month: Int): CachedBudget?
	suspend fun getFirstBudgetBefore(spaceId: String, year: Int, month: Int): CachedBudget?
}