package org.testadirapa.sesterzo.handlers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class MutableStateFlowCaptchaProgressHandler(
	private val stateFlow: MutableStateFlow<Double?>
) : CaptchaProgressHandler {
	override fun onCaptchaProgress(progress: Double) {
		stateFlow.update { progress }
	}
}