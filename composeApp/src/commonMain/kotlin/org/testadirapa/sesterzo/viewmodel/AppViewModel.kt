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
import org.testadirapa.sesterzo.viewmodel.state.CreateSpaceState
import org.testadirapa.sesterzo.viewmodel.state.MainScreenState
import org.testadirapa.sesterzo.viewmodel.state.RecoverKeyState
import org.testadirapa.sesterzo.viewmodel.state.StartupState
import kotlin.time.Duration.Companion.minutes

class AppViewModel : ViewModel() {
	private val logger = Logger.withTag("AppViewModel")

	private val _appState = MutableStateFlow<AppState>(StartupState)
	val appState: StateFlow<AppState> = _appState

	private val _errorState = MutableStateFlow<ErrorState?>(null)
	val errorState: StateFlow<ErrorState?> = _errorState

	private val _loadingState = MutableStateFlow(false)
	val loadingState: StateFlow<Boolean> = _loadingState

	private var jwtMonitorJob: Job? = null

	init {
		checkStorageAndMaybeInit()
	}

	fun acceptIntent(intent: Intent) {
		setLoading()
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
							updateStateOnKeyState(api)
						}
					}
					Intent.ConfirmBackup -> {
						AppCtx.api.user.setBackupConfirmation()
						_appState.update { mainScreenOrCreateSpace() }
					}
					is Intent.RestoreWithPrivateKey -> {
						expectStateAs<RecoverKeyState>(appState.value) {
							AppCtx.api = it.api.toFullApiWithPrivateKey(
								storage = PlatformContext.storageFacade(),
								cache = PlatformContext.persistentCache(),
								cacheTtl = BuildKonfig.cacheTtl.minutes,
								privateKey = intent.privateKey,
							)
							_appState.update { mainScreenOrCreateSpace() }
						}
					}
					is Intent.RestoreWithRecoveryKey -> {
						expectStateAs<RecoverKeyState>(appState.value) {
							AppCtx.api = it.api.toFullApiWithRecoveryKey(
								storage = PlatformContext.storageFacade(),
								cache = PlatformContext.persistentCache(),
								cacheTtl = BuildKonfig.cacheTtl.minutes,
								recoveryKey = intent.recoveryKey,
							)
							_appState.update { mainScreenOrCreateSpace() }
						}
					}
				}
			}.onFailure { error ->
				logger.e(error) { "Error processing intent: $intent" }
				_errorState.update { error.toErrorState() }
			}
		}
		unsetLoading()
	}

	fun setError(error: Throwable) {
		viewModelScope.launch {
			_errorState.update { error.toErrorState() }
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
					storage = PlatformContext.storageFacade(),
					cache = PlatformContext.persistentCache(),
					cacheTtl = BuildKonfig.cacheTtl.minutes,
				)
				updateStateOnKeyState(api)
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

	private suspend fun updateStateOnKeyState(api: SesterzoApi) {
		val currentUser = api.user.getCurrentUser()
		_appState.update {
			when (api) {
				is RecoverableSesterzoApi -> RecoverKeyState(api)
				is FullSesterzoApi -> {
					startMonitoringJwt(api)
					AppCtx.api = api
					if (currentUser.hasBackup) {
						mainScreenOrCreateSpace()
					} else {
						BackupPrivateKeyState
					}
				}
				else -> it
			}
		}
	}

	private suspend fun mainScreenOrCreateSpace() : AppState =
		if (AppCtx.api.space.getSpaces().isEmpty()) {
			CreateSpaceState(isFirst = true)
		} else {
			MainScreenState
		}

	fun setLoading() {
		_loadingState.update { true }
	}

	fun unsetLoading() {
		_loadingState.update { false }
	}
}