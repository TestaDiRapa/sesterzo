package org.testadirapa.sesterzo.services

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.testadirapa.sesterzo.api.AuthApi
import org.testadirapa.sesterzo.repository.PropertyRepository
import org.testadirapa.sesterzo.security.JwtPayload.Companion.isJwtExpiredOrInvalid
import kotlin.time.Duration.Companion.seconds

class AuthService(
	private val authApi: AuthApi,
	private val propertyRepository: PropertyRepository,
	initialJwt: String,
	initialRefresh: String
) {

	data class JwtState(val accessToken: String?, val refreshToken: String)
	class MissingJwtStateException : Exception()

	private val jwtMutex = Mutex()
	private val expirationPadding = 30.seconds

	private val _jwtState: MutableStateFlow<JwtState?> = MutableStateFlow(
		JwtState(accessToken = initialJwt, refreshToken = initialRefresh)
	)
	val jwtState: StateFlow<JwtState?>
		get() = _jwtState

	private suspend fun maybeRefreshTokenAndUpdateState() {
		jwtMutex.withLock {
			val (currentJwt, currentRefresh) = _jwtState.value
				?: throw MissingJwtStateException()
			when {
				isJwtExpiredOrInvalid(jwt = currentRefresh, refreshPadding = expirationPadding) -> {
					propertyRepository.resetJwt()
					propertyRepository.resetRefreshJwt()
					_jwtState.value = null
				}
				currentJwt == null || isJwtExpiredOrInvalid(jwt = currentJwt, refreshPadding = expirationPadding) -> {
					val newState = authApi.refresh(refreshToken = currentRefresh).bodyOrThrow()
					propertyRepository.setJwt(newState.jwt)
					_jwtState.value = JwtState(accessToken = newState.jwt, refreshToken = currentRefresh)
				}
			}
		}
	}

	suspend fun getJwtOrNull(): String? {
		maybeRefreshTokenAndUpdateState()
		return _jwtState.value?.accessToken
	}

	suspend fun getJwt(): String = getJwtOrNull() ?: throw MissingJwtStateException()

	suspend fun invalidateToken() {
		jwtMutex.withLock {
			_jwtState.value?.let { (_, refreshToken) ->
				propertyRepository.resetJwt()
				_jwtState.value = JwtState(accessToken = null, refreshToken = refreshToken)
			}
		}
	}

}