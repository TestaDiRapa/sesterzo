package org.testadirapa.sesterzo

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.testadirapa.sesterzo.config.configureControllers
import org.testadirapa.sesterzo.config.configureDAO
import org.testadirapa.sesterzo.config.configureExceptions
import org.testadirapa.sesterzo.config.configureHTTP
import org.testadirapa.sesterzo.config.configureKoin
import org.testadirapa.sesterzo.config.configureThrottling

fun main() {
	embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
		.start(wait = true)
}

fun Application.module() {
	configureHTTP()
	configureKoin()
	configureControllers()
	configureThrottling()
	configureExceptions()
	configureDAO()
}