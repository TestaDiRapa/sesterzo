package org.testadirapa.sesterzo.config

import org.testadirapa.sesterzo.storage.StorageFacade

expect object PlatformConfig {

	expect suspend fun storageFacade(): StorageFacade

}