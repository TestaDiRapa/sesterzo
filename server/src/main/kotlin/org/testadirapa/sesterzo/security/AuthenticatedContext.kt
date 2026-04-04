package org.testadirapa.sesterzo.security

import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import kotlinx.coroutines.withContext
import org.testadirapa.sesterzo.config.AUTH_CTX
import org.testadirapa.sesterzo.exceptions.JwtException

private suspend fun RoutingContext.withSecurityContext(
	block: suspend RoutingContext.() -> Unit,
) {
	val claims = call.principal<JWTPrincipal>()?.payload?.toJWTClaims()
		?: throw JwtException("No JWT passed in the request")
	withContext(SecurityContext(claims)) {
		block()
	}
}

fun Route.authenticatedGet(
	path: String,
	ctx: String = AUTH_CTX,
	block: suspend RoutingContext.() -> Unit,
): Route =
	authenticate(ctx) {
		get(path) {
			withSecurityContext(block)
		}
	}

fun Route.authenticatedPost(
	path: String,
	ctx: String = AUTH_CTX,
	block: suspend RoutingContext.() -> Unit,
): Route =
	authenticate(ctx) {
		post(path) {
			withSecurityContext(block)
		}
	}

fun Route.authenticatedDelete(
	path: String,
	ctx: String = AUTH_CTX,
	block: suspend RoutingContext.() -> Unit,
): Route =
	authenticate(ctx) {
		delete(path) {
			withSecurityContext(block)
		}
	}

fun Route.authenticatedPut(
	path: String,
	ctx: String = AUTH_CTX,
	block: suspend RoutingContext.() -> Unit,
): Route =
	authenticate(ctx) {
		put(path) {
			withSecurityContext(block)
		}
	}