package org.testadirapa.sesterzo.storage

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.UserNotAuthenticatedException
import com.icure.kryptom.crypto.AesAlgorithm.CbcWithPkcs7Padding
import com.icure.kryptom.crypto.AesKey
import com.icure.kryptom.crypto.defaultCryptoService
import com.icure.kryptom.utils.base64Decode
import com.icure.kryptom.utils.base64Encode
import org.testadirapa.sesterzo.exceptions.SecureStorageException
import org.testadirapa.sesterzo.storage.EncryptedStorageFacade.Companion.SECRET_KEY
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

@RequiresApi(Build.VERSION_CODES.R)
suspend fun getOrCreateSecretKey(
	storageFacade: StorageFacade,
	key: String,
	accessLevel: Set<SecureKeyAccessLevel>,
	authorizationTimeoutSeconds: Int
): AesKey<CbcWithPkcs7Padding> {
	return getSecretKey(key, storageFacade) ?: createSecretKey(accessLevel, key, storageFacade, authorizationTimeoutSeconds)
}

@RequiresApi(Build.VERSION_CODES.R)
suspend fun getSecretKey(key: String, storage: StorageFacade): AesKey<CbcWithPkcs7Padding>? {
	val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
	val iv = storage.getItem("$key.iv")?.let { base64Decode(it) }
	val cipherBytes = storage.getItem("$key.cipher")?.let { base64Decode(it) }

	if (iv == null && cipherBytes == null && !keyStore.containsAlias(key)) return null
	if (iv == null || cipherBytes == null) throw SecureStorageException("Missing IV or cipher bytes")
	if (!keyStore.containsAlias(key)) throw SecureStorageException("Key not found in keystore")

	val keyStoreKey = (keyStore.getEntry(key, null) as KeyStore.SecretKeyEntry).secretKey
	return decryptKey(cipherBytes, iv, keyStoreKey)
}

@RequiresApi(Build.VERSION_CODES.R)
private suspend fun createSecretKey(
	accessLevel: Set<SecureKeyAccessLevel>,
	key: String,
	storage: StorageFacade,
	authorizationTimeoutSeconds: Int
): AesKey<CbcWithPkcs7Padding> {
	val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
	val keyGenParameterSpec = KeyGenParameterSpec.Builder(key, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
		.setBlockModes(KeyProperties.BLOCK_MODE_CBC)
		.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
		.apply {
			if (accessLevel.isNotEmpty()) {
				setUserAuthenticationRequired(true)
				setUserAuthenticationParameters(authorizationTimeoutSeconds, accessLevel.fold(0) { acc, level -> acc or level.toKeyProperties() })
			} else {
				setUserAuthenticationRequired(false)
			}
		}
		.build()
	keyGenerator.init(keyGenParameterSpec)
	val keyStoreKey = keyGenerator.generateKey()
	val aesKey = defaultCryptoService.aes.generateKey(CbcWithPkcs7Padding)
	val (iv, cipherBytes) = encryptKey(aesKey, keyStoreKey)
	storage.setItem("$key.iv", base64Encode(iv))
	storage.setItem("$key.cipher", base64Encode(cipherBytes))
	return aesKey
}

@RequiresApi(Build.VERSION_CODES.R)
private suspend fun encryptKey(aesKey: AesKey<CbcWithPkcs7Padding>, secretKey: SecretKey): Pair<ByteArray, ByteArray> {
	return try {
		val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
		cipher.init(Cipher.ENCRYPT_MODE, secretKey)

		val iv = cipher.iv
		val cipherBytes = cipher.doFinal(defaultCryptoService.aes.exportKey(aesKey))
		Pair(iv, cipherBytes)
//	} catch (e: UserNotAuthenticatedException) {
//		throw SecureStorageException("User authentication required to encrypt key", e)
//	} catch (e: KeyPermanentlyInvalidatedException) {
//		throw SecureStorageException("Encryption key has been permanently invalidated", e)
	} catch (e: Exception) {
		throw IllegalStateException("Unexpected error during key encryption", e)
	}
}


@RequiresApi(Build.VERSION_CODES.R)
private suspend fun decryptKey(cipherBytes: ByteArray, iv: ByteArray, secretKey: SecretKey): AesKey<CbcWithPkcs7Padding> {
	return try {
		val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
		val spec = IvParameterSpec(iv)
		cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

		val aesKeyBytes = cipher.doFinal(cipherBytes)
		defaultCryptoService.aes.loadKey(CbcWithPkcs7Padding, aesKeyBytes)
//	} catch (e: UserNotAuthenticatedException) {
//		throw SecureStorageException("User authentication required to decrypt key", e)
//	} catch (e: KeyPermanentlyInvalidatedException) {
//		throw SecureStorageException("Decryption key has been permanently invalidated", e)
	} catch (e: Exception) {
		throw IllegalStateException("Unexpected error during key decryption", e)
	}
}

@RequiresApi(Build.VERSION_CODES.R)
private fun SecureKeyAccessLevel.toKeyProperties(): Int {
	return when (this) {
		SecureKeyAccessLevel.DevicePasscode -> KeyProperties.AUTH_DEVICE_CREDENTIAL
		SecureKeyAccessLevel.Biometric -> KeyProperties.AUTH_BIOMETRIC_STRONG
	}
}
