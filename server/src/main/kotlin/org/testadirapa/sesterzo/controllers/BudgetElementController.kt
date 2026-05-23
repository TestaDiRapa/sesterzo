package org.testadirapa.sesterzo.controllers

import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import org.testadirapa.sesterzo.logic.BudgetElementLogic
import org.testadirapa.sesterzo.model.EncryptedBudgetElement
import org.testadirapa.sesterzo.security.authenticateGetInSpace
import org.testadirapa.sesterzo.security.authenticatedPostInSpace
import org.testadirapa.sesterzo.utils.getIntPathParameter
import org.testadirapa.sesterzo.utils.getPathParameter
import kotlin.getValue

fun Routing.budgetElementController() = route("/budgetElement") {

	val budgetElementLogic by inject<BudgetElementLogic>()

	authenticateGetInSpace("/{budgetElementId}/latest") { spaceId ->
		val budgetElementId = call.getPathParameter("budgetElementId")
		call.respond(
			budgetElementLogic.getLatestVersionForId(
				spaceId = spaceId,
				budgetElementId = budgetElementId
			)
		)
	}

	authenticateGetInSpace("/{budgetElementId}/{version}") { spaceId ->
		val budgetElementId = call.getPathParameter("budgetElementId")
		val version = call.getIntPathParameter("version")
		call.respond(
			budgetElementLogic.getBudgetElement(
				spaceId = spaceId,
				budgetElementId = budgetElementId,
				version = version
			)
		)
	}

	authenticatedPostInSpace("") { spaceId ->
		val budgetElement = call.receive<EncryptedBudgetElement>()
		call.respond(
			budgetElementLogic.createBudgetElement(
				spaceId = spaceId,
				budgetElement = budgetElement
			)
		)
	}

}