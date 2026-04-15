package org.testadirapa.sesterzo.cache

interface PersistentCache {

	val budget: BudgetPersistentCache
	val budgetElement: BudgetElementPersistentCache
	val space: SpacePersistentCache
	val user: UserPersistentCache

}