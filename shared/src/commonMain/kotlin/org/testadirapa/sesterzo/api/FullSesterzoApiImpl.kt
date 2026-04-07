package org.testadirapa.sesterzo.api

import org.testadirapa.sesterzo.api.impl.FullRecoveryApiImpl
import org.testadirapa.sesterzo.api.impl.RecoveryApiImpl
import org.testadirapa.sesterzo.api.impl.SpaceApiImpl
import org.testadirapa.sesterzo.api.impl.UserApiImpl
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.services.CryptoService

class FullSesterzoApiImpl(
	private val httpConfig: HttpConfig,
	override val authService: AuthService,
	override val cryptoService: CryptoService
) : FullSesterzoApi {

	override val currentUserId: String get() = cryptoService.userId

	override val user: UserApi by lazy { UserApiImpl(httpConfig, authService) }

	override val recovery: FullRecoveryApi by lazy {
		FullRecoveryApiImpl(
			httpConfig = httpConfig,
			authService = authService,
			cryptoService = cryptoService
		)
	}

	override val spaceApi: SpaceApi by lazy {
		SpaceApiImpl(
			httpConfig = httpConfig,
			authService = authService,
			cryptoService = cryptoService
		)
	}

}