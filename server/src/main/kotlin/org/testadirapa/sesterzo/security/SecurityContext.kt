package org.testadirapa.sesterzo.security

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import org.testadirapa.sesterzo.model.UserSpaceRole
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
		fun <T> flowOnSecurityContext(block: suspend FlowCollector<T>.(SecurityContext) -> Unit): Flow<T> = flow {
			val securityContext = getFromCoroutineContext()
			block(securityContext)
		}
	}

	val currentUserId: String get() = jwtClaims.userId
	val spaces: Map<String, UserSpaceRole> = jwtClaims.spaces
}
