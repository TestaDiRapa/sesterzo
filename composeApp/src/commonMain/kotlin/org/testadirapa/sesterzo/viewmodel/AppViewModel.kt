package org.testadirapa.sesterzo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.BuildKonfig
import org.testadirapa.sesterzo.api.FullSesterzoApi
import org.testadirapa.sesterzo.api.RecoverableSesterzoApi
import org.testadirapa.sesterzo.api.SesterzoApi
import org.testadirapa.sesterzo.config.PlatformContext
import org.testadirapa.sesterzo.repository.PropertyRepository
import org.testadirapa.sesterzo.utils.expectStateAs
import org.testadirapa.sesterzo.viewmodel.errors.ErrorState
import org.testadirapa.sesterzo.viewmodel.errors.toErrorState
import org.testadirapa.sesterzo.viewmodel.state.AppState
import org.testadirapa.sesterzo.viewmodel.state.AuthenticateState
import org.testadirapa.sesterzo.viewmodel.state.BackupPrivateKeyState
import org.testadirapa.sesterzo.viewmodel.state.MainScreenState
import org.testadirapa.sesterzo.viewmodel.state.StartupState

class AppViewModel : ViewModel() {
	private val logger = Logger.withTag("AppViewModel")

	private val _appState = MutableStateFlow<AppState>(StartupState)
	val appState: StateFlow<AppState> = _appState

	private val _errorState = MutableStateFlow<ErrorState?>(null)
	val errorState: StateFlow<ErrorState?> = _errorState

	private var jwtMonitorJob: Job? = null

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
							val (tokens, api) = it.completeProcess(
								token = intent.token,
								storageFacade = PlatformContext.storageFacade()
							)
							AppCtx.propertyRepository.setJwt(tokens.jwt)
							AppCtx.propertyRepository.setRefreshJwt(tokens.refreshJwt)
							instantiateApiAndUpdateState(api)
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
			val jwt = propertyRepository.getJwt()
			val refreshJwt = propertyRepository.getRefreshJwt()
			if (jwt != null && refreshJwt != null) {
				val api = SesterzoApi.initializeWithTokens(
					baseUrl = BuildKonfig.apiUrl,
					jwt = jwt,
					refreshJwt = refreshJwt,
					storage = PlatformContext.storageFacade()
				)
				instantiateApiAndUpdateState(api)
			} else {
				_appState.update { AuthenticateState() }
			}

		}
	}

	private fun startMonitoringJwt(api: SesterzoApi) {
		jwtMonitorJob?.cancel()
		jwtMonitorJob = viewModelScope.launch {
			api.authService.jwtState.collect { state ->
				if (state == null) returnToLogin()
			}
		}
	}

	fun returnToLogin() {
		jwtMonitorJob?.cancel()
		jwtMonitorJob = null
		viewModelScope.launch {
			AppCtx.propertyRepository.clear()
			_appState.update { AuthenticateState() }
		}
	}

	private suspend fun instantiateApiAndUpdateState(api: SesterzoApi) {
		val currentUser = api.user.getCurrentUser().bodyOrThrow()
		_appState.update {
			if (api is RecoverableSesterzoApi) {
				TODO()
			} else if (api is FullSesterzoApi) {
				startMonitoringJwt(api)
				AppCtx.api = api
				if (currentUser.hasBackup) {
					MainScreenState
				} else {
					BackupPrivateKeyState
				}
			} else {
				it
			}
		}

	}
}