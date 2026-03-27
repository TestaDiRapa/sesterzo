package org.testadirapa.sesterzo.storage

interface StorageFacade {

	suspend fun getItem(key: String): String?

	suspend fun setItem(key: String, value: String)

	suspend fun removeItem(key: String)
}

expect fun getStorageFacade(): StorageFacade