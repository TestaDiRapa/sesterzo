package org.testadirapa.sesterzo

import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.testadirapa.sesterzo.config.configureControllers
import org.testadirapa.sesterzo.config.configureDAO
import org.testadirapa.sesterzo.config.configureExceptions
import org.testadirapa.sesterzo.config.configureHTTP
import org.testadirapa.sesterzo.config.configureKoin
import org.testadirapa.sesterzo.config.configureThrottling

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
	configureHTTP()
	configureKoin()
	configureThrottling()
	configureControllers()
	configureExceptions()
	configureDAO()
}