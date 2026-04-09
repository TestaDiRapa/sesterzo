package org.testadirapa.sesterzo.controllers

import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import org.testadirapa.sesterzo.logic.RecoveryLogic
import org.testadirapa.sesterzo.logic.UserLogic
import org.testadirapa.sesterzo.model.RecoveryKey
import org.testadirapa.sesterzo.model.dto.PublicKeyPayload
import org.testadirapa.sesterzo.security.Bip39
import org.testadirapa.sesterzo.security.authenticatedGet
import org.testadirapa.sesterzo.security.authenticatedPost

fun Routing.recoveryController() = route("/recoveryKey") {

	val recoveryLogic by inject<RecoveryLogic>()

	authenticatedGet("/bip39") {
		call.respond(Bip39.wordlist)
	}

	authenticatedGet("/{recoveryKeyId}") {
		val recoveryKeyId = call.parameters["recoveryKeyId"]
			?: throw IllegalArgumentException("recovery key id required")
		call.respond(recoveryLogic.getRecoveryKey(recoveryKeyId))
	}

	authenticatedPost("") {
		val recoveryKey = call.receive<RecoveryKey>()
		call.respond(recoveryLogic.createRecoveryKey(recoveryKey))
	}

}