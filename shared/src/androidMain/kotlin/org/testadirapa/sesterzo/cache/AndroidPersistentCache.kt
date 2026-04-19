package org.testadirapa.sesterzo.cache

import app.cash.sqldelight.db.SqlDriver

class AndroidPersistentCache(
	driver: SqlDriver
) : PersistentCache {

	override val budget: BudgetPersistentCache by lazy {
		AndroidBudgetPersistentCache(driver)
	}
	override val budgetElement: BudgetElementPersistentCache by lazy {
		AndroidBudgetElementPersistentCache(driver)
	}
	override val expense: ExpensePersistentCache by lazy {
		AndroidExpensePersistentCache(driver)
	}
	override val space: SpacePersistentCache by lazy {
		AndroidSpacePersistentCache(driver)
	}
	override val user: UserPersistentCache by lazy {
		AndroidUserPersistentCache(driver)
	}

}