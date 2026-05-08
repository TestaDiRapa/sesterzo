package org.testadirapa.sesterzo.api

import org.testadirapa.sesterzo.api.impl.AttachmentApiImpl
import org.testadirapa.sesterzo.api.impl.BudgetApiImpl
import org.testadirapa.sesterzo.api.impl.BudgetElementApiImpl
import org.testadirapa.sesterzo.api.impl.EntryApiImpl
import org.testadirapa.sesterzo.api.impl.FullRecoveryApiImpl
import org.testadirapa.sesterzo.api.impl.SpaceApiImpl
import org.testadirapa.sesterzo.api.impl.UserApiImpl
import org.testadirapa.sesterzo.cache.PersistentCache
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.services.CryptoService
import org.testadirapa.sesterzo.storage.StorageFacade
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

class FullSesterzoApiImpl(
	httpConfig: HttpConfig,
	cache: PersistentCache,
	cacheTtl: Duration,
	storage: StorageFacade,
	override val authService: AuthService,
	override val cryptoService: CryptoService
) : FullSesterzoApi {

	override val currentUserId: String get() = cryptoService.userId

	override val attachment: AttachmentApi by lazy {
		AttachmentApiImpl(
			httpConfig = httpConfig,
			cache = cache.attachment,
			authService = authService,
			ttl = 1.days,
			cryptoService = cryptoService
		)
	}

	override val budget: BudgetApi by lazy {
		BudgetApiImpl(
			httpConfig = httpConfig,
			cache = cache.budget,
			ttl = cacheTtl,
			authService = authService,
			cryptoService = cryptoService,
			spaceApi = space,
			budgetElementApi = budgetElement,
		)
	}

	override val budgetElement: BudgetElementApi by lazy {
		BudgetElementApiImpl(
			httpConfig = httpConfig,
			cache = cache.budgetElement,
			ttl = cacheTtl,
			authService = authService,
			cryptoService = cryptoService
		)
	}

	override val entry: EntryApi by lazy {
		EntryApiImpl(
			httpConfig = httpConfig,
			cache = cache.entry,
			authService = authService,
			cryptoService = cryptoService
		)
	}

	override val recovery: FullRecoveryApi by lazy {
		FullRecoveryApiImpl(
			httpConfig = httpConfig,
			authService = authService,
			cryptoService = cryptoService
		)
	}

	override val space: SpaceApi by lazy {
		SpaceApiImpl(
			httpConfig = httpConfig,
			cache = cache.space,
			ttl = cacheTtl,
			authService = authService,
			cryptoService = cryptoService
		)
	}

	override val user: UserApi by lazy {
		UserApiImpl(
			httpConfig = httpConfig,
			cache = cache.user,
			ttl = cacheTtl,
			localStorage = storage,
			authService = authService
		)
	}

}