package org.testadirapa.sesterzo.controllers

import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import org.testadirapa.sesterzo.logic.ErrorReportLogic
import org.testadirapa.sesterzo.model.ErrorReport
import org.testadirapa.sesterzo.security.authenticatedPost

fun Routing.errorReportController() = route("/error") {

	val errorReportLogic by inject<ErrorReportLogic>()

	authenticatedPost("") {
		val report = call.receive<ErrorReport>()
		call.respond(errorReportLogic.saveReport(report))
	}

}