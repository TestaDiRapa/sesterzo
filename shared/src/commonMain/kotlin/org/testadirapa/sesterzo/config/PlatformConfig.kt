package org.testadirapa.sesterzo.config

import org.testadirapa.sesterzo.storage.StorageFacade

expect object PlatformConfig {

	suspend fun storageFacade(): StorageFacade

}