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
import org.testadirapa.sesterzo.exceptions.InvalidSpaceAuthorizationException
import org.testadirapa.sesterzo.exceptions.JwtException
import org.testadirapa.sesterzo.model.UserSpaceRole
import org.testadirapa.sesterzo.utils.getPathParameter

private fun RoutingContext.extractJwtClaims(): JwtClaims =
	call.principal<JWTPrincipal>()?.payload?.toJWTClaims() ?: throw JwtException("No JWT passed in the request")

private fun JwtClaims.hasAtLeastRoleInSpace(requiredRole: UserSpaceRole, spaceId: String) {
	val role = spaces[spaceId]
	if (role == null || role.authLevel > requiredRole.authLevel) {
		throw InvalidSpaceAuthorizationException(spaceId)
	}
}

private suspend fun RoutingContext.withSecurityContext(
	block: suspend RoutingContext.() -> Unit,
) {
	withContext(SecurityContext(jwtClaims = extractJwtClaims())) {
		block()
	}
}

private suspend fun RoutingContext.withSecurityContextInSpace(
	jwtClaims: JwtClaims,
	spaceId: String,
	block: suspend RoutingContext.(spaceId: String) -> Unit,
) {
	withContext(SecurityContext(jwtClaims = jwtClaims)) {
		block(spaceId)
	}
}

private suspend fun RoutingContext.checkSpacePermissionsAndExecute(
	requiredRole: UserSpaceRole,
	block: suspend RoutingContext.(spaceId: String) -> Unit
) {
	val spaceId = call.getPathParameter("spaceId")
	val claims = extractJwtClaims()
	claims.hasAtLeastRoleInSpace(requiredRole, spaceId)
	withSecurityContextInSpace(
		jwtClaims = claims,
		spaceId = spaceId,
		block = block
	)
}

private fun withSpaceSegment(path: String) = buildString {
	append("/inSpace/{spaceId}")
	if(path.isNotBlank()) {
		append("/")
		append(path.trimStart('/'))
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

fun Route.authenticateGetInSpace(
	path: String,
	ctx: String = AUTH_CTX,
	requiredRole: UserSpaceRole = UserSpaceRole.User,
	block: suspend RoutingContext.(spaceId: String) -> Unit,
): Route =
	authenticate(ctx) {
		get(withSpaceSegment(path)) {
			checkSpacePermissionsAndExecute(requiredRole, block)
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

fun Route.authenticatedPostInSpace(
	path: String,
	ctx: String = AUTH_CTX,
	requiredRole: UserSpaceRole = UserSpaceRole.User,
	block: suspend RoutingContext.(spaceId: String) -> Unit,
): Route =
	authenticate(ctx) {
		post(withSpaceSegment(path)) {
			checkSpacePermissionsAndExecute(requiredRole, block)
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

fun Route.authenticatedDeleteInSpace(
	path: String,
	ctx: String = AUTH_CTX,
	requiredRole: UserSpaceRole = UserSpaceRole.User,
	block: suspend RoutingContext.(spaceId: String) -> Unit,
): Route =
	authenticate(ctx) {
		delete(withSpaceSegment(path)) {
			checkSpacePermissionsAndExecute(requiredRole, block)
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

fun Route.authenticatedPutInSpace(
	path: String,
	ctx: String = AUTH_CTX,
	requiredRole: UserSpaceRole = UserSpaceRole.User,
	block: suspend RoutingContext.(spaceId: String) -> Unit,
): Route =
	authenticate(ctx) {
		put(withSpaceSegment(path)) {
			checkSpacePermissionsAndExecute(requiredRole, block)
		}
	}