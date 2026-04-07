package org.testadirapa.sesterzo.services

import com.icure.kryptom.crypto.AesAlgorithm
import com.icure.kryptom.crypto.AesKey
import com.icure.kryptom.crypto.RsaAlgorithm
import com.icure.kryptom.crypto.RsaKeypair
import com.icure.kryptom.crypto.RsaService
import com.icure.kryptom.crypto.defaultCryptoService
import com.icure.kryptom.utils.base64Decode
import com.icure.kryptom.utils.base64Encode
import org.testadirapa.sesterzo.exceptions.InvalidPrivateKeyException
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.storage.StorageFacade

class CryptoService private constructor(
	private val keyPair: RsaKeypair<RsaAlgorithm.RsaEncryptionAlgorithm.OaepWithSha256>,
	val userId: String,
) {

	companion object {

		const val PRIVATE_KEY_STORAGE_KEY = "org.testadirapa.sesterzo.privateRsaKey"

		suspend fun initCreatingKeyPair(
			storage: StorageFacade,
			userId: String
		): CryptoService {
			val keyPair = defaultCryptoService.rsa.generateKeyPair(
				algorithm = RsaAlgorithm.RsaEncryptionAlgorithm.OaepWithSha256,
				keySize = RsaService.KeySize.Rsa2048
			)
			val privateKeyAsPkcs8 = defaultCryptoService.rsa.exportPrivateKeyPkcs8(keyPair.private)
			storage.setItem(PRIVATE_KEY_STORAGE_KEY, base64Encode(privateKeyAsPkcs8))
			return CryptoService(keyPair, userId)
		}

		suspend fun initWithExistingKeyPair(
			privateKey: Base64String,
			publicKey: Base64String,
			userId: String
		): CryptoService {
			val publicKeySpki = defaultCryptoService.rsa.loadPublicKeySpki(
				algorithm = RsaAlgorithm.RsaEncryptionAlgorithm.OaepWithSha256,
				publicKeySpki = base64Decode(publicKey)
			)
			val privateKeyPkcs8 = defaultCryptoService.rsa.loadPrivateKeyPkcs8(
				algorithm = RsaAlgorithm.RsaEncryptionAlgorithm.OaepWithSha256,
				privateKeyPkcs8 = base64Decode(privateKey)
			)
			val keyPair = RsaKeypair(privateKeyPkcs8, publicKeySpki)
			verifyKeyPair(keyPair)
			return CryptoService(keyPair = keyPair, userId = userId)
		}

		private suspend fun verifyKeyPair(keyPair: RsaKeypair<RsaAlgorithm.RsaEncryptionAlgorithm.OaepWithSha256>) {
			runCatching {
				val test = defaultCryptoService.strongRandom.randomBytes(10)
				val encrypted = defaultCryptoService.rsa.encrypt(test, keyPair.public)
				defaultCryptoService.rsa.decrypt(encrypted, keyPair.private)
			}.onFailure {
				throw InvalidPrivateKeyException()
			}
		}
	}

	private val spaceKeys: MutableMap<String, AesKey<AesAlgorithm.CbcWithPkcs7Padding>> = mutableMapOf()

	suspend fun exportAndEncodePublicKey(): Base64String =
		base64Encode(defaultCryptoService.rsa.exportPublicKeySpki(keyPair.public))

	suspend fun exportPrivateKey(): ByteArray = defaultCryptoService.rsa.exportPrivateKeyPkcs8(keyPair.private)

	suspend fun exportAndEncodePrivateKey(): Base64String = base64Encode(exportPrivateKey())

	suspend fun decryptAndLoadSpaceKey(space: Space) {
		if(!spaceKeys.containsKey(space.id)) {
			space.users[userId]?.also { accessKey ->
				val decryptedKey = defaultCryptoService.rsa.decrypt(
					data = base64Decode(accessKey.encryptedKey),
					privateKey = keyPair.private
				)
				val aesKey = defaultCryptoService.aes.loadKey(
					algorithm = AesAlgorithm.CbcWithPkcs7Padding,
					bytes = decryptedKey
				)
				spaceKeys[space.id] = aesKey
			}
		}
	}
}