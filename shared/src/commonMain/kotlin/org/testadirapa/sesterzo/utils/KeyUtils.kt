package org.testadirapa.sesterzo.utils

import com.icure.kryptom.crypto.AesAlgorithm.CbcWithPkcs7Padding
import com.icure.kryptom.crypto.AesKey
import com.icure.kryptom.crypto.PrivateRsaKey
import com.icure.kryptom.crypto.PublicRsaKey
import com.icure.kryptom.crypto.RsaAlgorithm
import com.icure.kryptom.crypto.defaultCryptoService
import com.icure.kryptom.utils.base32Decode
import com.icure.kryptom.utils.base64Decode
import org.testadirapa.sesterzo.exceptions.CannotDecryptWithAesKeyException
import org.testadirapa.sesterzo.exceptions.CannotDecryptWithRsaKeyException
import org.testadirapa.sesterzo.exceptions.CannotEncryptWithAesKeyException
import org.testadirapa.sesterzo.exceptions.InvalidKeyFormatException
import org.testadirapa.sesterzo.model.Base32String
import org.testadirapa.sesterzo.model.Base64String

fun decodeBase32Key(
	key: Base32String
): ByteArray = try { base32Decode(key) } catch (_: Exception) { throw InvalidKeyFormatException() }

fun decodeBase64Key(
	key: Base64String
): ByteArray = try { base64Decode(key) } catch (_: Exception) { throw InvalidKeyFormatException() }

suspend fun loadAesKey(
	keyBytes: ByteArray,
): AesKey<CbcWithPkcs7Padding> = try {
	defaultCryptoService.aes.loadKey(
		algorithm = CbcWithPkcs7Padding,
		bytes = keyBytes,
	)
} catch (_: Exception) { throw InvalidKeyFormatException() }

suspend fun loadPublicRsaKeySpki(
	publicKeyBytes: ByteArray,
) : PublicRsaKey<RsaAlgorithm.RsaEncryptionAlgorithm.OaepWithSha256> = try {
	defaultCryptoService.rsa.loadPublicKeySpki(
		algorithm = RsaAlgorithm.RsaEncryptionAlgorithm.OaepWithSha256,
		publicKeySpki = publicKeyBytes
	)
} catch (_: Exception) { throw InvalidKeyFormatException() }

suspend fun loadPrivateRsaKeyPkcs8(
	privateKeyBytes: ByteArray,
) : PrivateRsaKey<RsaAlgorithm.RsaEncryptionAlgorithm.OaepWithSha256> = try {
	defaultCryptoService.rsa.loadPrivateKeyPkcs8(
		algorithm = RsaAlgorithm.RsaEncryptionAlgorithm.OaepWithSha256,
		privateKeyPkcs8 = privateKeyBytes
	)
} catch (_: Exception) { throw InvalidKeyFormatException() }

suspend fun aesDecrypt(
	encryptedData: ByteArray,
	key: AesKey<CbcWithPkcs7Padding>
): ByteArray = try {
	defaultCryptoService.aes.decrypt(
		ivAndEncryptedData = encryptedData,
		key = key
	)
} catch (_: Exception) { throw CannotDecryptWithAesKeyException() }

suspend fun aesEncrypt(
	dataToEncrypt: ByteArray,
	key: AesKey<CbcWithPkcs7Padding>
): ByteArray = try {
	defaultCryptoService.aes.encrypt(
		data = dataToEncrypt,
		key = key
	)
} catch (_: Exception) { throw CannotEncryptWithAesKeyException() }

suspend fun rsaDecrypt(
	encryptedData: ByteArray,
	privateKey: PrivateRsaKey<RsaAlgorithm.RsaEncryptionAlgorithm>
): ByteArray = try {
	defaultCryptoService.rsa.decrypt(
		data = encryptedData,
		privateKey = privateKey
	)
} catch (_: Exception) { throw CannotDecryptWithRsaKeyException() }