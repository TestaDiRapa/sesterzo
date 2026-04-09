package org.testadirapa.sesterzo.api.impl

import com.icure.kryptom.crypto.AesAlgorithm.CbcWithPkcs7Padding
import com.icure.kryptom.crypto.defaultCryptoService
import com.icure.kryptom.utils.base64Encode
import com.icure.kryptom.utils.base64UrlEncode
import org.testadirapa.sesterzo.api.FullRecoveryApi
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.model.Bip39RecoveryKey
import org.testadirapa.sesterzo.model.RecoveryKey
import org.testadirapa.sesterzo.model.Timestamp
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.services.CryptoService

open class FullRecoveryApiImpl(
	httpConfig: HttpConfig,
	authService: AuthService,
	private val cryptoService: CryptoService,
) : RecoveryApiImpl(httpConfig, authService), FullRecoveryApi {

	override suspend fun generateRecoveryKey(
		owner: String,
		expiresAt: Timestamp?
	): Bip39RecoveryKey {
		val privateKey = cryptoService.exportPrivateKey()
		val recoveryKey = defaultCryptoService.aes.generateKey(CbcWithPkcs7Padding)
		val encryptedPrivateKey = base64Encode(
			defaultCryptoService.aes.encrypt(
				data = privateKey,
				key = recoveryKey
			)
		)
		val exportedRecoveryKey = defaultCryptoService.aes.exportKey(recoveryKey)
		val recoveryKeyHash = base64UrlEncode(
			defaultCryptoService.digest.sha256(exportedRecoveryKey)
		)
		createRecoveryKey(
			RecoveryKey(
				id = recoveryKeyHash,
				expiresAt = expiresAt,
				owner = owner,
				encryptedKey = encryptedPrivateKey
			)
		).bodyOrThrow()
		return bip39Encode(exportedRecoveryKey)
	}
}