package org.testadirapa.sesterzo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.config.PlatformContext
import org.testadirapa.sesterzo.repository.PropertyRepository
import org.testadirapa.sesterzo.utils.expectStateAs
import org.testadirapa.sesterzo.viewmodel.errors.ErrorState
import org.testadirapa.sesterzo.viewmodel.errors.toErrorState
import org.testadirapa.sesterzo.viewmodel.state.AppState
import org.testadirapa.sesterzo.viewmodel.state.AuthenticateState
import org.testadirapa.sesterzo.viewmodel.state.MainPageState
import org.testadirapa.sesterzo.viewmodel.state.StartupState
import kotlin.time.Duration.Companion.seconds

class AppViewModel : ViewModel() {
	private val logger = Logger.withTag("AppViewModel")

	private val _appState = MutableStateFlow<AppState>(StartupState)
	val appState: StateFlow<AppState> = _appState

	private val _errorState = MutableStateFlow<ErrorState?>(null)
	val errorState: StateFlow<ErrorState?> = _errorState

	init {
		checkStorageAndMaybeInit()
	}

	fun acceptIntent(intent: Intent) {
		viewModelScope.launch {
			runCatching {
				when (intent) {
					is Intent.StartRegistration -> {
						expectStateAs<AuthenticateState>(appState.value) {
							it.startRegistrationProcess(email = intent.email, name = intent.name)
						}
					}
					is Intent.StartLogin -> {
						expectStateAs<AuthenticateState>(appState.value) {
							it.startLoginProcess(email = intent.email)
						}
					}
					is Intent.CompleteAuthentication -> {
						expectStateAs<AuthenticateState>(appState.value) {
							AppCtx.api = it.completeProcess(intent.token)
							_appState.update { MainPageState }
						}
					}
				}
			}.onFailure { error ->
				logger.e(error) { "Error processing intent: $intent" }
				_errorState.update { error.toErrorState() }
			}
		}
	}

	fun dismissError() {
		_errorState.update { null }
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