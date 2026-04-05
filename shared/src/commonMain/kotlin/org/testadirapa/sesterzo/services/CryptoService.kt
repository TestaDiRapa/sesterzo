package org.testadirapa.sesterzo.services

import com.icure.kryptom.crypto.RsaAlgorithm
import com.icure.kryptom.crypto.RsaKeypair
import com.icure.kryptom.crypto.RsaService
import com.icure.kryptom.crypto.defaultCryptoService
import com.icure.kryptom.utils.base64Decode
import com.icure.kryptom.utils.base64Encode
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.storage.StorageFacade

class CryptoService private constructor(
	private val keyPair: RsaKeypair<RsaAlgorithm.RsaEncryptionAlgorithm.OaepWithSha256>
) {

	companion object {

		const val PRIVATE_KEY_STORAGE_KEY = "org.testadirapa.sesterzo.privateRsaKey"

		suspend fun initCreatingKeyPair(
			storage: StorageFacade
		): CryptoService {
			val keyPair = defaultCryptoService.rsa.generateKeyPair(
				algorithm = RsaAlgorithm.RsaEncryptionAlgorithm.OaepWithSha256,
				keySize = RsaService.KeySize.Rsa2048
			)
			val privateKeyAsPkcs8 = defaultCryptoService.rsa.exportPrivateKeyPkcs8(keyPair.private)
			storage.setItem(PRIVATE_KEY_STORAGE_KEY, base64Encode(privateKeyAsPkcs8))
			return CryptoService(keyPair)
		}

		suspend fun initWithExistingKeyPair(
			privateKey: Base64String,
			publicKey: Base64String
		): CryptoService {
			val publicKeySpki = defaultCryptoService.rsa.loadPublicKeySpki(
				algorithm = RsaAlgorithm.RsaEncryptionAlgorithm.OaepWithSha256,
				publicKeySpki = base64Decode(publicKey)
			)
			val privateKeyPkcs8 = defaultCryptoService.rsa.loadPrivateKeyPkcs8(
				algorithm = RsaAlgorithm.RsaEncryptionAlgorithm.OaepWithSha256,
				privateKeyPkcs8 = base64Decode(privateKey)
			)
			return CryptoService(RsaKeypair(privateKeyPkcs8, publicKeySpki))
		}
	}

	suspend fun exportAndEncodePublicKey(): Base64String =
		base64Encode(defaultCryptoService.rsa.exportPublicKeySpki(keyPair.public))

	suspend fun exportPrivateKey(): ByteArray = defaultCryptoService.rsa.exportPrivateKeyPkcs8(keyPair.private)

	suspend fun exportAndEncodePrivateKey(): Base64String = base64Encode(exportPrivateKey())
}