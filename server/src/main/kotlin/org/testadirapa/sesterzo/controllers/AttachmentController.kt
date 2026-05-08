package org.testadirapa.sesterzo.controllers

import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.testadirapa.sesterzo.logic.AttachmentLogic
import org.testadirapa.sesterzo.security.authenticateGetInSpace
import org.testadirapa.sesterzo.utils.getPathParameter

fun Routing.attachmentController() = route("/attachment") {

	val attachmentLogic by inject<AttachmentLogic>()

	authenticateGetInSpace("/{attachmentId}") { spaceId ->
		val attachmentId = call.getPathParameter("attachmentId")
		call.respond(attachmentLogic.getAttachment(spaceId = spaceId, attachmentId = attachmentId))
	}
}