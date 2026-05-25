package org.testadirapa.sesterzo.controllers

import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import org.testadirapa.sesterzo.logic.BudgetLogic
import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.EncryptedBudget
import org.testadirapa.sesterzo.model.VersionableReference
import org.testadirapa.sesterzo.security.authenticateGetInSpace
import org.testadirapa.sesterzo.security.authenticatedPostInSpace
import org.testadirapa.sesterzo.security.authenticatedPutInSpace
import org.testadirapa.sesterzo.utils.getIntPathParameter
import org.testadirapa.sesterzo.utils.getPathParameter
import kotlin.getValue

fun Routing.budgetController() = route("/budget") {

	val budgetLogic by inject<BudgetLogic>()

	authenticateGetInSpace("/{budgetId}") { spaceId ->
		val budgetId = call.getPathParameter("budgetId")
		call.respond(budgetLogic.getBudget(spaceId = spaceId, budgetId = budgetId))
	}

	authenticatedPostInSpace("") { spaceId ->
		val budget = call.receive<EncryptedBudget>()
		call.respond(budgetLogic.createBudget(spaceId = spaceId, budget = budget))
	}

	authenticatedPostInSpace("/encryptedSelf") { spaceId ->
		val budget = call.receive<EncryptedBudget>()
		call.respond(budgetLogic.setEncryptedSelfOnBudget(spaceId = spaceId, budget = budget))
	}

	authenticateGetInSpace("/forYear/{year}") { spaceId ->
		val year = call.getIntPathParameter("year")
		call.respond(budgetLogic.getBudgetsForYear(spaceId = spaceId, year = year))
	}

	authenticateGetInSpace("/firstBefore/{year}/{month}") { spaceId ->
		val year = call.getIntPathParameter("year")
		val month = call.getIntPathParameter("month")
		call.respond(budgetLogic.getFirstBudgetBefore(spaceId = spaceId, year = year, month = month))
	}

	authenticateGetInSpace("/firstAfter/{year}/{month}") { spaceId ->
		val year = call.getIntPathParameter("year")
		val month = call.getIntPathParameter("month")
		call.respond(budgetLogic.getFirstBudgetAfter(spaceId = spaceId, year = year, month = month))
	}

	authenticatedPostInSpace("/{budgetId}/{templateType}") { spaceId ->
		val budgetId = call.getPathParameter("budgetId")
		val templateType = call.getPathParameter("templateType")
		val inclusiveStart = call.queryParameters["inclusiveStart"]?.toBoolean() ?: false
		val elementReference = call.receive<VersionableReference>()
		call.respond(
			budgetLogic.updateTemplateVersionOnBudgets(
				spaceId = spaceId,
				budgetId = budgetId,
				inclusiveStart = inclusiveStart,
				type = BudgetElement.BudgetElementType.valueOf(templateType),
				budgetElementReference = elementReference
			)
		)
	}

}