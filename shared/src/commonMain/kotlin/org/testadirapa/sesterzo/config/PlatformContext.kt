package org.testadirapa.sesterzo.config

import org.testadirapa.sesterzo.storage.StorageFacade

expect object PlatformContext {

	suspend fun storageFacade(): StorageFacade

}