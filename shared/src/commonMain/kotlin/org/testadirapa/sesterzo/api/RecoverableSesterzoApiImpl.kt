package org.testadirapa.sesterzo.api

import com.icure.kryptom.utils.base64Encode
import org.testadirapa.sesterzo.api.impl.RecoveryApiImpl
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Bip39RecoveryKey
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.services.CryptoService
import org.testadirapa.sesterzo.services.CryptoService.Companion.PRIVATE_KEY_STORAGE_KEY
import org.testadirapa.sesterzo.storage.StorageFacade

class RecoverableSesterzoApiImpl(
	private val httpConfig: HttpConfig,
	override val authService: AuthService,
	override val user: UserApi
) : RecoverableSesterzoApi {

	override val recovery: RecoveryApi by lazy {
		RecoveryApiImpl(
			httpConfig = httpConfig,
			authService = authService,
		)
	}

	override suspend fun toFullApiWithRecoveryKey(
		storage: StorageFacade,
		recoveryKey: Bip39RecoveryKey
	): FullSesterzoApi {
		val privateKeyBytes = recovery.recoverKey(recoveryKey)
		return toFullApiWithPrivateKey(
			storage = storage,
			privateKey = base64Encode(privateKeyBytes),
		)
	}

	override suspend fun toFullApiWithPrivateKey(
		storage: StorageFacade,
		privateKey: Base64String
	): FullSesterzoApi {
		val currentUser = user.getCurrentUser()
		val cryptoService = CryptoService.initWithExistingKeyPair(
			publicKey = currentUser.publicKey ?: throw IllegalStateException("Missing public key"),
			privateKey = privateKey,
			userId = currentUser.id,
		)
		storage.setItem(PRIVATE_KEY_STORAGE_KEY, cryptoService.exportAndEncodePrivateKey())
		return FullSesterzoApiImpl(
			httpConfig = httpConfig,
			authService = authService,
			cryptoService = cryptoService,
		)
	}

}