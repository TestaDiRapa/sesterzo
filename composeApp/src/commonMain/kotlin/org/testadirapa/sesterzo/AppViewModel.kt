package org.testadirapa.sesterzo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.testadirapa.sesterzo.config.PlatformContext
import org.testadirapa.sesterzo.repository.PropertyRepository
import kotlin.time.Duration.Companion.seconds

sealed interface AppState {
	data object Startup : AppState
	data object Authenticate : AppState
}

class AppViewModel : ViewModel() {
	private val _appState = MutableStateFlow<AppState>(AppState.Startup)
	val appState: StateFlow<AppState> = _appState

	init {
		checkStorageAndMaybeInit()
	}

	fun checkStorageAndMaybeInit() {
		viewModelScope.launch {
			val propertyRepository = PropertyRepository(
				datastore = PlatformContext.storageFacade()
			)
			AppCtx.propertyRepository = propertyRepository
			delay(3.seconds)
			_appState.update {
				AppState.Authenticate
			}
		}
	}
}