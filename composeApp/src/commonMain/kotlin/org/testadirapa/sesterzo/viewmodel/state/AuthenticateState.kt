package org.testadirapa.sesterzo.viewmodel.state

import kotlinx.coroutines.flow.MutableStateFlow
import org.testadirapa.sesterzo.BuildKonfig
import org.testadirapa.sesterzo.api.SesterzoApi
import org.testadirapa.sesterzo.api.processes.Process
import org.testadirapa.sesterzo.handlers.MutableStateFlowCaptchaProgressHandler
import org.testadirapa.sesterzo.model.dto.AuthResponse
import org.testadirapa.sesterzo.storage.StorageFacade

class AuthenticateState : AppState {

	private lateinit var process: Process
	val captchaStateFlow = MutableStateFlow<MutableStateFlowCaptchaProgressHandler.CaptchaProgress>(
		MutableStateFlowCaptchaProgressHandler.CaptchaProgress.Uninitialized
	)

	suspend fun startRegistrationProcess(email: String, name: String) {
		process = SesterzoApi.initializeRegistrationProcess(
			baseUrl = BuildKonfig.apiUrl,
			email = email,
			name = name,
			captchaHandler = MutableStateFlowCaptchaProgressHandler(captchaStateFlow)
		)
	}

	suspend fun startLoginProcess(email: String) {
		process = SesterzoApi.initializeLoginProcess(
			baseUrl = BuildKonfig.apiUrl,
			email = email,
			captchaHandler = MutableStateFlowCaptchaProgressHandler(captchaStateFlow)
		)
	}

	suspend fun completeProcess(token: String, storageFacade: StorageFacade): Pair<AuthResponse, SesterzoApi> =
		process.complete(token, storageFacade)
}
