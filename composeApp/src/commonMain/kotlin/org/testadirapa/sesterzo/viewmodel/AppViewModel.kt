package org.testadirapa.sesterzo.viewmodel

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
import org.testadirapa.sesterzo.styles.colors.randomSpaceColor
import org.testadirapa.sesterzo.utils.expectStateAs
import org.testadirapa.sesterzo.viewmodel.errors.ErrorState
import org.testadirapa.sesterzo.viewmodel.errors.toErrorState
import org.testadirapa.sesterzo.viewmodel.intents.AppIntent
import org.testadirapa.sesterzo.viewmodel.state.AppState
import org.testadirapa.sesterzo.viewmodel.state.AuthenticateState
import org.testadirapa.sesterzo.viewmodel.state.BackupPrivateKeyState
import org.testadirapa.sesterzo.viewmodel.state.CreateSpaceState
import org.testadirapa.sesterzo.viewmodel.state.MainScreenState
import org.testadirapa.sesterzo.viewmodel.state.RecoverKeyState
import org.testadirapa.sesterzo.viewmodel.state.StartupState
import kotlin.time.Duration.Companion.minutes

class AppViewModel : AbstractViewModel<AppIntent>() {
	override val logger = Logger.withTag("AppViewModel")

	private val _appState = MutableStateFlow<AppState>(StartupState)
	val appState: StateFlow<AppState> = _appState

	private val _errorState = MutableStateFlow<ErrorState?>(null)
	val errorState: StateFlow<ErrorState?> = _errorState

	private var jwtMonitorJob: Job? = null

	init {
		checkStorageAndMaybeInit()
	}

	override suspend fun processIntent(intent: AppIntent) {
		when (intent) {
			is AppIntent.StartRegistration -> {
				expectStateAs<AuthenticateState>(appState.value) {
					it.startRegistrationProcess(email = intent.email, name = intent.name)
				}
			}
			is AppIntent.StartLogin -> {
				expectStateAs<AuthenticateState>(appState.value) {
					it.startLoginProcess(email = intent.email)
				}
			}
			is AppIntent.CompleteAuthentication -> {
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
			AppIntent.ConfirmBackup -> {
				AppCtx.api.user.setBackupConfirmation()
				_appState.update { mainScreenOrCreateSpace() }
			}
			is AppIntent.RestoreWithPrivateKey -> {
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
			is AppIntent.RestoreWithRecoveryKey -> {
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
			is AppIntent.CreateSpaceIntent -> {
				val space = AppCtx.api.space.createSpace(
					name = intent.name,
					picture = intent.picture,
					fallbackColor = intent.color.rgbColor
				)
				AppCtx.propertyRepository.setDefaultSpace(space.id)
				_appState.update { MainScreenState(initialSpace = space) }
			}
			is AppIntent.NavigateToSpaceCreationIntent -> {
				_appState.update {
					CreateSpaceState(currentSpace = intent.currentSpace)
				}
			}
			is AppIntent.NavigateToMainScreen -> {
				_appState.update {
					MainScreenState(
						initialSpace = intent.space
					)
				}
			}
		}
	}

	override fun onError(error: Throwable) {
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

	private suspend fun mainScreenOrCreateSpace() : AppState {
		val spaces = AppCtx.api.space.getSpaces()
		return if (spaces.isEmpty()) {
			CreateSpaceState(currentSpace = null)
		} else {
			MainScreenState(
				initialSpace = AppCtx.propertyRepository.getDefaultSpace()?.let {
					AppCtx.api.space.getSpace(spaceId = it, bypassCache = false)
				} ?: spaces.first(),
			)
		}

	}

}