package org.testadirapa.sesterzo.storage

class VolatileStorageFacade: StorageFacade {
	private val localStorage = mutableMapOf<String, String>()

	override suspend fun getItem(key: String): String? = localStorage[key]

	override suspend fun setItem(key: String, value: String) {
		localStorage[key] = value
	}

	override suspend fun removeItem(key: String) {
		localStorage.remove(key)
	}

}

actual fun getStorageFacade(): StorageFacade = VolatileStorageFacade()