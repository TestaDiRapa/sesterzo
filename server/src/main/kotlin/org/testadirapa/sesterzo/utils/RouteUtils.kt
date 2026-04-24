package org.testadirapa.sesterzo.utils

import io.ktor.server.routing.RoutingCall

fun RoutingCall.getPathParameter(parameter: String): String =
	checkNotNull(parameters[parameter]) { "Parameter $parameter is required" }

fun RoutingCall.getIntPathParameter(parameter: String): Int =
	checkNotNull(parameters[parameter]) { "Parameter $parameter is required" }
		.toIntOrNull() ?: throw IllegalArgumentException("Parameter $parameter is not an integer")