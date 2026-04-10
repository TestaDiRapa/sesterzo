package org.testadirapa.sesterzo.config

import org.testadirapa.sesterzo.cache.PersistentCache
import org.testadirapa.sesterzo.storage.StorageFacade

expect object PlatformContext {

	suspend fun storageFacade(): StorageFacade

	suspend fun persistentCache(): PersistentCache

}