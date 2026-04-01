package org.testadirapa.sesterzo.storage

import android.os.Build
import androidx.annotation.RequiresApi
import org.testadirapa.sesterzo.security.AndroidBiometricAuthenticator
import org.testadirapa.sesterzo.security.authenticateUsingBiometric
import org.testadirapa.sesterzo.storage.EncryptedStorageFacade.Companion.SECRET_KEY

@RequiresApi(Build.VERSION_CODES.R)
class AndroidBiometricSecureStorageFacade(
	private val storage: StorageFacade,
	private val biometricAuthenticator: AndroidBiometricAuthenticator,
	private val accessLevel: Set<SecureKeyAccessLevel>,
	private val biometricAuthenticationTitle: String,
	private val authorizationTimeoutSeconds: Int = 0
) : StorageFacade {

	private lateinit var encryptedStorageFacade: EncryptedStorageFacade

	private suspend fun getEncryptedFacade(): EncryptedStorageFacade {
		if (!::encryptedStorageFacade.isInitialized) {
			biometricAuthenticator.authenticateUsingBiometric(biometricAuthenticationTitle)
			val encryptionKey = getOrCreateSecretKey(storage, SECRET_KEY, accessLevel, authorizationTimeoutSeconds)
			encryptedStorageFacade = EncryptedStorageFacade(storage, encryptionKey)
		}
		return encryptedStorageFacade
	}

	override suspend fun getItem(key: String): String? = getEncryptedFacade().getItem(key)

	override suspend fun setItem(key: String, value: String) {
		getEncryptedFacade().setItem(key, value)
	}

	override suspend fun removeItem(key: String) {
		getEncryptedFacade().removeItem(key)
	}
}