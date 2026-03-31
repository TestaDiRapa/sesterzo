package org.testadirapa.sesterzo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface AppState {

	data object Startup : AppState

}

class AppViewModel : ViewModel() {
	private val _appState = MutableStateFlow<AppState>(AppState.Startup)
	val appState: StateFlow<AppState> = _appState
}