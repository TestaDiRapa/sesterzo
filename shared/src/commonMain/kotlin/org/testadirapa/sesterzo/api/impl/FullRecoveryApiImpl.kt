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

	private suspend fun doGenerateRecoveryKey(
		receiver: String?,
		expiresAt: Timestamp?
	): ByteArray {
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
				owner = cryptoService.userId,
				receiver = receiver ?: cryptoService.userId,
				encryptedKey = encryptedPrivateKey
			)
		).bodyOrThrow()
		return exportedRecoveryKey
	}

	override suspend fun generateRecoveryKey(
		receiver: String?,
		expiresAt: Timestamp?
	): Bip39RecoveryKey = bip39Encode(
		doGenerateRecoveryKey(receiver = receiver, expiresAt = expiresAt)
	)

	override suspend fun generateRecoveryKeyAndReturnBipIndexes(
		receiver: String?,
		expiresAt: Timestamp?
	): List<Int> = bip39EncodeAsIndexes(
		doGenerateRecoveryKey(receiver = receiver, expiresAt = expiresAt)
	)

	override suspend fun recoverKey(bip39RecoveryKey: Bip39RecoveryKey): ByteArray {
		return super.recoverKey(bip39RecoveryKey)
	}
}