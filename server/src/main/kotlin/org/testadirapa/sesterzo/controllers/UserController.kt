package org.testadirapa.sesterzo.controllers

import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import org.testadirapa.sesterzo.logic.UserLogic
import org.testadirapa.sesterzo.model.dto.PublicKeyPayload
import org.testadirapa.sesterzo.security.authenticatedGet
import org.testadirapa.sesterzo.security.authenticatedPost

fun Routing.userController() = route("/user") {

	val userLogic by inject<UserLogic>()

	authenticatedGet("/current") {
		call.respond(userLogic.getCurrentUser())
	}

	authenticatedPost("/current/publicKey") {
		val publicKey = call.receive<PublicKeyPayload>().publicKey
		call.respond(userLogic.setPublicKey(publicKey))
	}

}