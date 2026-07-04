package org.testadirapa.sesterzo.controllers

import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import org.testadirapa.sesterzo.logic.UserLogic
import org.testadirapa.sesterzo.model.Currency
import org.testadirapa.sesterzo.model.dto.PublicKeyPayload
import org.testadirapa.sesterzo.security.authenticatedGet
import org.testadirapa.sesterzo.security.authenticatedPost
import org.testadirapa.sesterzo.security.authenticatedPut
import org.testadirapa.sesterzo.utils.getBooleanPathParameter
import org.testadirapa.sesterzo.utils.getPathParameter

fun Routing.userController() = route("/user") {

	val userLogic by inject<UserLogic>()

	authenticatedGet("/current") {
		call.respond(userLogic.getCurrentUser())
	}

	authenticatedPost("/byIds") {
		val userIds = call.receive<Set<String>>()
		call.respond(userLogic.getUsers(userIds))
	}


	authenticatedPost("/current/publicKey") {
		val publicKey = call.receive<PublicKeyPayload>().publicKey
		call.respond(userLogic.setPublicKey(publicKey))
	}

	authenticatedPut("/current/hasBackup") {
		call.respond(userLogic.setBackupConfirmation())
	}

	authenticatedPut("/current/name/{name}") {
		val name = call.getPathParameter("name")
		call.respond(userLogic.setName(name))
	}

	authenticatedPut("/current/currency/{currency}") {
		val currency = Currency.valueOf(call.getPathParameter("currency"))
		call.respond(userLogic.setCurrency(currency))
	}

	authenticatedPut("/current/logsOptIn/{optIn}") {
		val optIn = call.getBooleanPathParameter("optIn")
		call.respond(userLogic.setLogOptIn(optIn))
	}

}