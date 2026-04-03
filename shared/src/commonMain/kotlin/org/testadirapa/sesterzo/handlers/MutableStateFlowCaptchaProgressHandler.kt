package org.testadirapa.sesterzo.handlers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class MutableStateFlowCaptchaProgressHandler(
	private val stateFlow: MutableStateFlow<CaptchaProgress>
) : CaptchaProgressHandler {

	sealed class CaptchaProgress {
		data object Uninitialized : CaptchaProgress()
		data class Loading(val value: Double) : CaptchaProgress()
		data object Complete : CaptchaProgress()

		val loadingValue: Double? get() = if (this is Loading) value else null
		val isComplete: Boolean get() = this is Complete
		val isUninitialised: Boolean get() = this is Uninitialized
	}

	init {
		stateFlow.update { CaptchaProgress.Uninitialized }
	}

	override fun onCaptchaProgress(progress: Double) {
		stateFlow.update {
			if (progress < 1.0) {
				CaptchaProgress.Loading(progress)
			} else {
				CaptchaProgress.Complete
			}
		}
	}
}