package org.testadirapa.sesterzo.config

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.testadirapa.sesterzo.storage.AndroidSecureStorageFacade
import org.testadirapa.sesterzo.storage.DataStorePreferenceStorage
import org.testadirapa.sesterzo.storage.SecureKeyAccessLevel
import org.testadirapa.sesterzo.storage.StorageFacade
import kotlin.time.Duration.Companion.minutes

actual object PlatformConfig {

	private val durationBetweenBiometricPrompts = 2.minutes

	private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(
		name = applicationId
	)

	private var _applicationId: String? = null
	val applicationId: String
		get() = checkNotNull(_applicationId) { "PlatformConfig was not initialized" }

	private val storageFacade: StorageFacade by lazy {
		checkNotNull(_applicationContext) { "PlatformConfig was not initialized" }.let {
			DataStorePreferenceStorage(it.appDataStore)
		}
	}

	private var _applicationContext: Application? = null
	private var _biometricManager: BiometricManager? = null
	private var secureCardinalStorageFacade: StorageFacade? = null

	fun setup(application: Application) {
		_applicationContext = application
		_biometricManager = BiometricManager.from(application)
	}

	actual suspend fun storageFacade(): StorageFacade {
		if (secureCardinalStorageFacade == null) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
				storageFacade
			} else {
				val biometricManager = checkNotNull(_biometricManager) { "PlatformConfig was not initialized" }
				val accessLevels = when (BiometricManager.BIOMETRIC_SUCCESS) {
					biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ->
						setOf(SecureKeyAccessLevel.Biometric)

					biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL) ->
						setOf(SecureKeyAccessLevel.DevicePasscode)

					else -> emptySet()
				}

				AndroidSecureStorageFacade(
					storage = storageFacade,
					accessLevel = accessLevels,
					authorizationTimeoutSeconds = durationBetweenBiometricPrompts.inWholeSeconds.toInt()
				)
			}
		}

		return secureCardinalStorageFacade!!
	}

}