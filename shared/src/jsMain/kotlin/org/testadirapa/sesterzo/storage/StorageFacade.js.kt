package org.testadirapa.sesterzo.storage

import kotlinx.browser.localStorage

class LocalStorageJsFacade : StorageFacade {
	init {
		check(eval("typeof(localStorage) === 'object'") as Boolean) {
			"No global local storage is available"
		}
	}

	override suspend fun getItem(key: String): String? =
		localStorage.getItem(key)

	override suspend fun setItem(key: String, value: String) =
		localStorage.setItem(key, value)

	override suspend fun removeItem(key: String) =
		localStorage.removeItem(key)
}