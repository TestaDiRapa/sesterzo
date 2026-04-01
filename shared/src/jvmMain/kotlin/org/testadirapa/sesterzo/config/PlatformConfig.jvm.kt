package org.testadirapa.sesterzo.config

import org.testadirapa.sesterzo.storage.StorageFacade
import org.testadirapa.sesterzo.storage.VolatileStorageFacade

actual object PlatformContext {
	actual suspend fun storageFacade(): StorageFacade = VolatileStorageFacade()
}