package org.testadirapa.sesterzo.config

import org.testadirapa.sesterzo.storage.LocalStorageJsFacade
import org.testadirapa.sesterzo.storage.StorageFacade

actual object PlatformContext {
	actual suspend fun storageFacade(): StorageFacade = LocalStorageJsFacade()
}