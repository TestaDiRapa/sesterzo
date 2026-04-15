package org.testadirapa.sesterzo.controllers

import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import org.testadirapa.sesterzo.logic.BudgetElementLogic
import org.testadirapa.sesterzo.security.authenticateGetInSpace
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

}