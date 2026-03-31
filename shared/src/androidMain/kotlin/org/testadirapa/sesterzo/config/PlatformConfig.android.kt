package org.testadirapa.sesterzo.config

import android.app.Application
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import org.testadirapa.sesterzo.storage.AndroidSecureStorageFacade
import org.testadirapa.sesterzo.storage.DataStorePreferenceStorage
import org.testadirapa.sesterzo.storage.SecureKeyAccessLevel
import org.testadirapa.sesterzo.storage.StorageFacade
import kotlin.time.Duration.Companion.minutes

actual object PlatformConfig {

	private val durationBetweenBiometricPrompts = 2.minutes

	private var _applicationId: String? = null

	private var _appDataStore: DataStore<Preferences>? = null

	private val storageFacade: StorageFacade by lazy {
		checkNotNull(_appDataStore) { "PlatformConfig was not initialized" }.let {
			DataStorePreferenceStorage(it)
		}
	}

	private var _applicationContext: Application? = null
	private var _biometricManager: BiometricManager? = null
	private var secureCardinalStorageFacade: StorageFacade? = null

	fun setup(application: Application) {
		_applicationId = application.packageName
		_applicationContext = application
		_biometricManager = BiometricManager.from(application)
		_appDataStore = PreferenceDataStoreFactory.create(
			produceFile = { application.preferencesDataStoreFile(application.packageName) }
		)
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