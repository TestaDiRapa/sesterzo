package org.testadirapa.sesterzo.utils

import io.ktor.server.routing.RoutingCall

fun RoutingCall.getPathParameter(parameter: String): String =
	checkNotNull(parameters[parameter]) { "Parameter $parameter is required" }