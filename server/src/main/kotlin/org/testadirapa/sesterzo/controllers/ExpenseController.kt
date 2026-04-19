package org.testadirapa.sesterzo.controllers

import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import org.testadirapa.sesterzo.logic.ExpenseLogic
import org.testadirapa.sesterzo.security.authenticateGetInSpace
import org.testadirapa.sesterzo.security.authenticatedDeleteInSpace
import org.testadirapa.sesterzo.utils.getPathParameter
import kotlin.getValue

fun Routing.expenseController() = route("/expense") {

	val expenseLogic by inject<ExpenseLogic>()

	authenticateGetInSpace("/forBudget/{budgetId}/all") { spaceId ->
		val budgetId = call.getPathParameter("budgetId")
		call.respond(
			expenseLogic.getExpensesForBudget(spaceId = spaceId, budgetId = budgetId)
		)
	}

	authenticateGetInSpace("/forBudget/{budgetId}/after/{timestamp}") { spaceId ->
		val budgetId = call.getPathParameter("budgetId")
		val timestamp = call.getPathParameter("timestamp").toLong()
		call.respond(
			expenseLogic.getExpensesForBudgetAfter(spaceId = spaceId, budgetId = budgetId, after = timestamp)
		)
	}

	authenticatedDeleteInSpace("/{expenseId}") { spaceId ->
		val expenseId = call.getPathParameter("expenseId")
		call.respond(
			expenseLogic.deleteExpense(spaceId = spaceId, expenseId = expenseId)
		)
	}
}