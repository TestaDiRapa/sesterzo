package org.testadirapa.sesterzo.controllers

import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.testadirapa.sesterzo.logic.SpaceLogic
import org.testadirapa.sesterzo.model.SpaceStub
import org.testadirapa.sesterzo.security.authenticatedGet
import org.testadirapa.sesterzo.security.authenticatedPost
import org.testadirapa.sesterzo.utils.getPathParameter

fun Routing.spaceController() = route("/space") {

	val spaceLogic by inject<SpaceLogic>()

	authenticatedGet("") {
		call.respond(spaceLogic.getSpaces())
	}

	authenticatedGet("/{spaceId}") {
		val spaceId = call.getPathParameter("spaceId")
		call.respond(spaceLogic.getSpace(spaceId))
	}

	authenticatedPost("") {
		val spaceStub = call.receive<SpaceStub>()
		call.respond(spaceLogic.createSpace(spaceStub))
	}

}