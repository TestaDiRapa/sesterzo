package org.testadirapa.sesterzo.storage

import kotlin.js.JsString

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("() => typeof localStorage === 'object'")
private external fun isLocalStorageAvailable(): Boolean

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(key) => localStorage.getItem(key)")
private external fun localStorageGetItem(key: JsString): JsString?

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(key, value) => localStorage.setItem(key, value)")
private external fun localStorageSetItem(key: JsString, value: JsString)

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(key) => localStorage.removeItem(key)")
private external fun localStorageRemoveItem(key: JsString)

@OptIn(ExperimentalWasmJsInterop::class)
class LocalStorageWasmJsFacade : StorageFacade {
	init {
		check(isLocalStorageAvailable()) {
			"No global local storage is available"
		}
	}

	override suspend fun getItem(key: String): String? =
		localStorageGetItem(key.toJsString())?.toString()

	override suspend fun setItem(key: String, value: String) =
		localStorageSetItem(key.toJsString(), value.toJsString())

	override suspend fun removeItem(key: String) =
		localStorageRemoveItem(key.toJsString())
}

actual fun getStorageFacade(): StorageFacade = LocalStorageWasmJsFacade()