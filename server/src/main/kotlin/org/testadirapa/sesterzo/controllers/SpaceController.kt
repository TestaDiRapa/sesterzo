package org.testadirapa.sesterzo.controllers

import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.testadirapa.sesterzo.logic.SpaceLogic
import org.testadirapa.sesterzo.security.authenticatedGet

fun Routing.spaceController() = route("/space") {

	val spaceLogic by inject<SpaceLogic>()

	authenticatedGet("") {
		call.respond(spaceLogic.getSpaces())
	}

}