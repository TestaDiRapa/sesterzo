package org.testadirapa.sesterzo.api.processes

import org.testadirapa.sesterzo.api.AuthApi
import org.testadirapa.sesterzo.api.SesterzoApi.Companion.getHttpConfig
import org.testadirapa.sesterzo.api.impl.AuthApiImpl
import org.testadirapa.sesterzo.api.impl.SesterzoApiImpl
import org.testadirapa.sesterzo.model.dto.AuthResponse
import org.testadirapa.sesterzo.services.AuthService

sealed class Process(
	private val baseUrl: String,
) {

	abstract suspend fun completeProcess(authApi: AuthApi, token: String): AuthResponse

	suspend fun complete(token: String): SesterzoApiImpl {
		val httpConfig = getHttpConfig(baseUrl)
		val authApi = AuthApiImpl(config = httpConfig)
		val tokens = completeProcess(authApi, token)
		val authService = AuthService(
			authApi = authApi,
			initialJwt = tokens.jwt,
			initialRefresh = tokens.refreshJwt
		)
		return SesterzoApiImpl(
			httpConfig = httpConfig,
			authService = authService
		)
	}

}