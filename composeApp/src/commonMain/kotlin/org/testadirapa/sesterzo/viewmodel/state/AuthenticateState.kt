package org.testadirapa.sesterzo.viewmodel.state

import kotlinx.coroutines.flow.MutableStateFlow
import org.testadirapa.sesterzo.BuildKonfig
import org.testadirapa.sesterzo.api.SesterzoApi
import org.testadirapa.sesterzo.api.processes.Process
import org.testadirapa.sesterzo.handlers.MutableStateFlowCaptchaProgressHandler

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

	suspend fun completeProcess(token: String): SesterzoApi =
		process.complete(token)
}
