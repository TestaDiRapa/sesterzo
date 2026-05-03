package org.testadirapa.sesterzo.api

import com.icure.kerberus.Solution
import com.icure.kerberus.resolveChallenge
import com.icure.kryptom.crypto.defaultCryptoService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.coroutineScope
import org.testadirapa.sesterzo.api.impl.AuthApiImpl
import org.testadirapa.sesterzo.api.impl.UserApiImpl
import org.testadirapa.sesterzo.api.processes.LoginProcess
import org.testadirapa.sesterzo.api.processes.RegistrationProcess
import org.testadirapa.sesterzo.cache.PersistentCache
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.handlers.CaptchaProgressHandler
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Bip39RecoveryKey
import org.testadirapa.sesterzo.model.dto.StartRegistrationData
import org.testadirapa.sesterzo.serialization.Serialization
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.services.CryptoService
import org.testadirapa.sesterzo.services.CryptoService.Companion.PRIVATE_KEY_STORAGE_KEY
import org.testadirapa.sesterzo.storage.StorageFacade
import org.testadirapa.sesterzo.utils.newPlatformHttpClient
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface SesterzoApi {

	companion object {

		private fun createHttpClient(): HttpClient =
			newPlatformHttpClient {
				install(ContentNegotiation) {
					json(json = Serialization.json)
				}
				install(HttpTimeout) {
					requestTimeoutMillis = 60_000
				}
			}

		val sharedHttpClient: HttpClient by lazy {
			createHttpClient()
		}

		fun getHttpConfig(baseUrl: String): HttpConfig = HttpConfig(
			baseUrl = baseUrl,
			client = sharedHttpClient,
			maxRetriesOnServerError = 3,
			delayOnRetry = 1.seconds
		)

		private suspend fun getAndSolveCaptcha(
			authApi: AuthApi,
			captchaHandler: CaptchaProgressHandler
		): Solution {
			val captchaId = defaultCryptoService.strongRandom.randomUUID()
			val captchaChallenge = authApi.getCaptchaChallenge(input = captchaId).bodyOrThrow()
			return resolveChallenge(
				config = captchaChallenge.challenge,
				serializedInput = captchaChallenge.input,
				onProgress = captchaHandler::onCaptchaProgress
			)
		}

		suspend fun initializeRegistrationProcess(
			baseUrl: String,
			email: String,
			name: String,
			captchaHandler: CaptchaProgressHandler
		): RegistrationProcess = coroutineScope {
			val httpConfig = getHttpConfig(baseUrl)
			val authApi = AuthApiImpl(config = httpConfig)
			val captchaSolution = getAndSolveCaptcha(authApi, captchaHandler)
			val registrationProcessId = authApi.startRegistration(
				registrationData = StartRegistrationData(
					email = email,
					name = name,
					captchaSolution = captchaSolution,
				)
			).bodyOrThrow()
			RegistrationProcess(processId = registrationProcessId, baseUrl = baseUrl)
		}

		suspend fun initializeLoginProcess(
			baseUrl: String,
			email: String,
			captchaHandler: CaptchaProgressHandler
		): LoginProcess = coroutineScope {
			val httpConfig = getHttpConfig(baseUrl)
			val authApi = AuthApiImpl(config = httpConfig)
			val captchaSolution = getAndSolveCaptcha(authApi, captchaHandler)
			authApi.getOtt(
				email = email,
				captchaSolution = captchaSolution,
			).bodyOrThrow()
			LoginProcess(email = email, baseUrl = baseUrl)
		}

		suspend fun initializeWithTokens(
			baseUrl: String,
			jwt: String,
			refreshJwt: String,
			storage: StorageFacade,
			cache: PersistentCache,
			cacheTtl: Duration
		): SesterzoApi = coroutineScope {
			val httpConfig = getHttpConfig(baseUrl)
			val authApi = AuthApiImpl(config = httpConfig)
			val authService = AuthService(
				authApi = authApi,
				initialJwt = jwt,
				initialRefresh = refreshJwt
			)
			val userApi = UserApiImpl(
				httpConfig = httpConfig,
				cache = cache.user,
				ttl = cacheTtl,
				localStorage = storage,
				authService = authService
			)
			val currentUser = userApi.getCurrentUser()
			val privateKey = storage.getItem(PRIVATE_KEY_STORAGE_KEY)
			val cryptoService = when {
				currentUser.publicKey == null -> CryptoService.initCreatingKeyPair(
					storage = storage,
					userId = currentUser.id,
				)
				privateKey != null -> CryptoService.initWithExistingKeyPair(
					publicKey = currentUser.publicKey,
					privateKey = privateKey,
					userId = currentUser.id,
				)
				else -> null
			}
			if (cryptoService != null) {
				if (currentUser.publicKey == null) {
					userApi.setPublicKeyForCurrentUser(
						cryptoService.exportAndEncodePublicKey()
					)
				}
				FullSesterzoApiImpl(
					httpConfig = httpConfig,
					authService = authService,
					cache = cache,
					cacheTtl = cacheTtl,
					storage = storage,
					cryptoService = cryptoService
				)
			} else {
				RecoverableSesterzoApiImpl(
					httpConfig = httpConfig,
					authService = authService,
					user = userApi
				)
			}
		}
	}

	val authService: AuthService
	val user: UserApi
	val recovery: RecoveryApi
}

interface RecoverableSesterzoApi : SesterzoApi {
	suspend fun toFullApiWithRecoveryKey(
		storage: StorageFacade,
		cache: PersistentCache,
		cacheTtl: Duration,
		recoveryKey: Bip39RecoveryKey
	): FullSesterzoApi
	suspend fun toFullApiWithPrivateKey(
		storage: StorageFacade,
		cache: PersistentCache,
		cacheTtl: Duration,
		privateKey: Base64String
	): FullSesterzoApi
}

interface FullSesterzoApi : SesterzoApi {
	val currentUserId: String

	val cryptoService: CryptoService

	val budget: BudgetApi
	val budgetElement: BudgetElementApi
	val entry: EntryApi
	override val recovery: FullRecoveryApi
	val space: SpaceApi
}