package org.testadirapa.sesterzo.api

import org.testadirapa.sesterzo.model.DecryptedExpense

interface ExpenseApi {

	/**
	 * Retrieves all the expenses in a space for a budget, where [DecryptedExpense.deleted] is false and in descending
	 * order by [DecryptedExpense.updated].
	 *
	 * - If [bypassCache] is true, it will retrieve all the expenses from the server.
	 * - Else, it will first load all the cached expenses and retrieve the ones created or updated after the most recent
	 * in the cache. If any of the ones retrieved from the server was updated or deleted, only the most recent version
	 * is returned and updated in cache.
	 *
	 * Also deleted elements are kept in the cache, for future reference.
	 *
	 * @param spaceId the id of the space.
	 * @param budgetId the id of the budget.
	 * @param bypassCache whether to bypass the local cache.
	 * @return a [List] of [DecryptedExpense] that are not deleted, in descending order by [DecryptedExpense.updated].
	 */
	suspend fun getInSpaceForBudget(spaceId: String, budgetId: String, bypassCache: Boolean): List<DecryptedExpense>
}