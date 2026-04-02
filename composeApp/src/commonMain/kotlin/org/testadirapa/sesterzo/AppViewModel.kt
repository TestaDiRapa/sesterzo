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
import org.testadirapa.sesterzo.utils.expectStateAs
import org.testadirapa.sesterzo.viewmodel.Intent
import org.testadirapa.sesterzo.viewmodel.state.AppState
import org.testadirapa.sesterzo.viewmodel.state.AuthenticateState
import org.testadirapa.sesterzo.viewmodel.state.StartupState
import kotlin.time.Duration.Companion.seconds

class AppViewModel : ViewModel() {
	private val _appState = MutableStateFlow<AppState>(StartupState)
	val appState: StateFlow<AppState> = _appState

	init {
		checkStorageAndMaybeInit()
	}

	fun acceptIntent(intent: Intent) {
		when (intent) {
			is Intent.StartRegistration -> {
				expectStateAs<AuthenticateState>(appState.value) {
					viewModelScope.launch {
						it.startRegistrationProcess(
							email = intent.email,
							name = intent.name
						)
					}
				}
			}
		}
	}

	fun checkStorageAndMaybeInit() {
		viewModelScope.launch {
			val propertyRepository = PropertyRepository(
				datastore = PlatformContext.storageFacade()
			)
			AppCtx.propertyRepository = propertyRepository
			delay(3.seconds)
			_appState.update {
				AuthenticateState()
			}
		}
	}
}