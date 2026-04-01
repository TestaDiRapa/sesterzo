package org.testadirapa.sesterzo.config

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.fragment.app.FragmentActivity
import org.testadirapa.sesterzo.security.AndroidBiometricAuthenticator
import org.testadirapa.sesterzo.storage.AndroidBiometricSecureStorageFacade
import org.testadirapa.sesterzo.storage.DataStorePreferenceStorage
import org.testadirapa.sesterzo.storage.SecureKeyAccessLevel
import org.testadirapa.sesterzo.storage.StorageFacade
import kotlin.time.Duration.Companion.minutes

actual object PlatformContext {

	private val durationBetweenBiometricPrompts = 2.minutes

	private var _applicationId: String? = null

	private var _appDataStore: DataStore<Preferences>? = null

	private val storageFacade: StorageFacade by lazy {
		DataStorePreferenceStorage(
			checkNotNull(_appDataStore) { "PlatformContext was not initialized" }
		)
	}

	private var _applicationContext: Application? = null
	private var _biometricManager: BiometricManager? = null
	@SuppressLint("StaticFieldLeak")
	private var _biometricAuthenticator: AndroidBiometricAuthenticator? = null
	private var secureCardinalStorageFacade: StorageFacade? = null
	private var _unlockStorageTitle: String? = null

	fun setup(
		application: Application,
		fragmentActivityContext: FragmentActivity,
		unlockStorageTitle: String
	) {
		_applicationId = application.packageName
		_applicationContext = application
		val manager = BiometricManager.from(application)
		_biometricManager = manager
		_biometricAuthenticator = AndroidBiometricAuthenticator(
			context = fragmentActivityContext,
			highestAuthAvailable = getHighestAuthOptionAvailable(manager)
		)
		_appDataStore = PreferenceDataStoreFactory.create(
			produceFile = { application.preferencesDataStoreFile(application.packageName) }
		)
		_unlockStorageTitle = unlockStorageTitle
	}

	private fun getHighestAuthOptionAvailable(biometricManager: BiometricManager): SecureKeyAccessLevel? =
		when (BiometricManager.BIOMETRIC_SUCCESS) {
			biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) ->
				SecureKeyAccessLevel.Biometric
			biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL) ->
				SecureKeyAccessLevel.DevicePasscode
			else -> null
		}

	actual suspend fun storageFacade(): StorageFacade {
		if (secureCardinalStorageFacade == null) {
			secureCardinalStorageFacade = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
				storageFacade
			} else {
				val biometricManager = checkNotNull(_biometricManager) { "PlatformContext was not initialized" }
				val accessLevels = setOfNotNull(getHighestAuthOptionAvailable(biometricManager))

				AndroidBiometricSecureStorageFacade(
					storage = storageFacade,
					biometricAuthenticator = _biometricAuthenticator!!,
					accessLevel = accessLevels,
					biometricAuthenticationTitle = _unlockStorageTitle!!,
					authorizationTimeoutSeconds = durationBetweenBiometricPrompts.inWholeSeconds.toInt()
				)
			}
		}

		return secureCardinalStorageFacade!!
	}

}