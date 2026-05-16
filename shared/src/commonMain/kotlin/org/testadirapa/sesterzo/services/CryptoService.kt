package org.testadirapa.sesterzo.services

import com.icure.kryptom.crypto.AesAlgorithm
import com.icure.kryptom.crypto.AesKey
import com.icure.kryptom.crypto.RsaAlgorithm
import com.icure.kryptom.crypto.RsaKeypair
import com.icure.kryptom.crypto.RsaService
import com.icure.kryptom.crypto.defaultCryptoService
import com.icure.kryptom.utils.base64Decode
import com.icure.kryptom.utils.base64Encode
import io.ktor.utils.io.core.toByteArray
import kotlinx.serialization.json.JsonObject
import org.testadirapa.sesterzo.exceptions.InvalidPrivateKeyException
import org.testadirapa.sesterzo.exceptions.MissingSpaceKeyException
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.DecryptedData
import org.testadirapa.sesterzo.model.EncryptedData
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.model.SpaceData
import org.testadirapa.sesterzo.serialization.Serialization
import org.testadirapa.sesterzo.storage.StorageFacade
import org.testadirapa.sesterzo.utils.aesDecrypt
import org.testadirapa.sesterzo.utils.aesEncrypt
import org.testadirapa.sesterzo.utils.decodeBase64Key
import org.testadirapa.sesterzo.utils.loadAesKey
import org.testadirapa.sesterzo.utils.loadPrivateRsaKeyPkcs8
import org.testadirapa.sesterzo.utils.loadPublicRsaKeySpki
import org.testadirapa.sesterzo.utils.rsaDecrypt

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
			val publicKeySpki = loadPublicRsaKeySpki(decodeBase64Key(publicKey))
			val privateKeyPkcs8 = loadPrivateRsaKeyPkcs8(decodeBase64Key(privateKey))
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
				val decryptedKey = rsaDecrypt(
					encryptedData = base64Decode(accessKey.encryptedKey),
					privateKey = keyPair.private
				)
				val aesKey = loadAesKey(decryptedKey)
				spaceKeys[space.id] = aesKey
			}
		}
	}

	suspend fun generateAndEncryptSpaceKey(): Base64String {
		val rawSpaceKey = defaultCryptoService.aes.exportKey(
			defaultCryptoService.aes.generateKey(AesAlgorithm.CbcWithPkcs7Padding)
		)
		return defaultCryptoService.rsa.encrypt(
			data = rawSpaceKey,
			publicKey = keyPair.public
		).let {
			base64Encode(it)
		}
	}

	suspend fun <E, D : DecryptedData<E>> decrypt(encryptedEntity: E): D where E : EncryptedData<D>, E : SpaceData {
		val decryptedFields = encryptedEntity.encryptedSelf?.let { encryptedSelf ->
			val key = spaceKeys[encryptedEntity.spaceId]
				?: run {
					throw MissingSpaceKeyException(encryptedEntity.spaceId)
				}
			aesDecrypt(
				encryptedData = base64Decode(encryptedSelf),
				key = key
			).let {
				Serialization.json.decodeFromString<JsonObject>(it.decodeToString())
			}
		} ?: JsonObject(mapOf())
		return encryptedEntity.toDecryptedData(decryptedFields = decryptedFields)
	}

	suspend fun <E: EncryptedData<D>, D> encrypt(decryptedEntity: D): E where D : DecryptedData<E>, D : SpaceData {
		val dataToEncrypt = decryptedEntity.getJsonToEncrypt()
		val encryptedSelf = dataToEncrypt.takeIf { it.isNotEmpty() }?.let {
			val key = spaceKeys[decryptedEntity.spaceId]
				?: throw MissingSpaceKeyException(decryptedEntity.spaceId)
			val jsonString = Serialization.json.encodeToString(it)
			base64Encode(
				aesEncrypt(
					dataToEncrypt = jsonString.toByteArray(),
					key = key
				)
			)
		}
		return decryptedEntity.toEncryptedEntity(encryptedSelf)
	}
}