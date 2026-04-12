package org.testadirapa.sesterzo.api.processes

import org.testadirapa.sesterzo.api.AuthApi
import org.testadirapa.sesterzo.api.SesterzoApi
import org.testadirapa.sesterzo.api.SesterzoApi.Companion.getHttpConfig
import org.testadirapa.sesterzo.api.impl.AuthApiImpl
import org.testadirapa.sesterzo.cache.PersistentCache
import org.testadirapa.sesterzo.model.dto.AuthResponse
import org.testadirapa.sesterzo.storage.StorageFacade
import kotlin.time.Duration

sealed class Process(
	private val baseUrl: String,
) {

	protected abstract suspend fun completeProcess(authApi: AuthApi, token: String): AuthResponse

	suspend fun complete(
		token: String,
		storage: StorageFacade,
		cache: PersistentCache,
		cacheTtl: Duration
	): Pair<AuthResponse, SesterzoApi> {
		val httpConfig = getHttpConfig(baseUrl)
		val authApi = AuthApiImpl(config = httpConfig)
		val tokens = completeProcess(authApi, token)
		return tokens to SesterzoApi.initializeWithTokens(
			baseUrl = baseUrl,
			jwt = tokens.jwt,
			refreshJwt = tokens.refreshJwt,
			storage = storage,
			cache = cache,
			cacheTtl = cacheTtl
		)
	}

}