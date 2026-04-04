package org.testadirapa.sesterzo.security

import kotlinx.coroutines.currentCoroutineContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

data class SecurityContext(
	private val jwtClaims: JwtClaims,
) : AbstractCoroutineContextElement(Key) {
	object Key: CoroutineContext.Key<SecurityContext>

	companion object {
		suspend fun getFromCoroutineContext(): SecurityContext =
			currentCoroutineContext()[Key]
				?: throw IllegalStateException("SecurityContext not found in coroutine context")
		suspend inline fun <T> withSecurityContext(block: suspend SecurityContext.() -> T): T =
			with(getFromCoroutineContext()) {
				block()
			}
	}

	val currentUserId: String get() = jwtClaims.userId
}
