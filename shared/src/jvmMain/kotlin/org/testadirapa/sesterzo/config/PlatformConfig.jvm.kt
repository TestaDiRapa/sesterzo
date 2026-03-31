package org.testadirapa.sesterzo.config

import org.testadirapa.sesterzo.security.BiometricAuthenticator
import org.testadirapa.sesterzo.security.NoOpBiometricAuthenticator
import org.testadirapa.sesterzo.storage.StorageFacade
import org.testadirapa.sesterzo.storage.VolatileStorageFacade

actual object PlatformContext {
	actual suspend fun storageFacade(): StorageFacade = VolatileStorageFacade()

	actual suspend fun biometricAuthenticator(): BiometricAuthenticator = NoOpBiometricAuthenticator
}