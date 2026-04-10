package org.testadirapa.sesterzo.config

import org.testadirapa.sesterzo.cache.PersistentCache
import org.testadirapa.sesterzo.storage.StorageFacade
import org.testadirapa.sesterzo.storage.VolatileStorageFacade

actual object PlatformContext {
	actual suspend fun storageFacade(): StorageFacade = VolatileStorageFacade()

	actual suspend fun persistentCache(): PersistentCache {
		throw UnsupportedOperationException("Persistent cache is not supported on JVM")
	}
}