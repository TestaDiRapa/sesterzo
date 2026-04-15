package org.testadirapa.sesterzo.config

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import org.testadirapa.sesterzo.controllers.authController
import org.testadirapa.sesterzo.controllers.budgetController
import org.testadirapa.sesterzo.controllers.budgetElementController
import org.testadirapa.sesterzo.controllers.recoveryController
import org.testadirapa.sesterzo.controllers.spaceController
import org.testadirapa.sesterzo.controllers.userController

fun Application.configureControllers() {
	install(ContentNegotiation) {
		json()
	}
	routing {
		authController()
		budgetController()
		budgetElementController()
		recoveryController()
		spaceController()
		userController()
	}
}