package org.testadirapa.sesterzo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.testadirapa.sesterzo.viewmodel.intents.Intent

abstract class AbstractViewModel<I : Intent>: ViewModel() {
	protected abstract val logger: Logger

	private val _loadingState = MutableStateFlow(false)
	val loadingState: StateFlow<Boolean> = _loadingState

	fun acceptIntent(intent: I) {
		setLoading()
		viewModelScope.launch {
			runCatching {
				processIntent(intent)
			}.onFailure { error ->
				logger.e(error) { "Error processing intent: $intent" }
				onError(error)
			}
		}
		unsetLoading()
	}

	protected abstract suspend fun processIntent(intent: I)
	abstract fun onError(error: Throwable)

	fun setLoading() {
		_loadingState.update { true }
	}

	fun unsetLoading() {
		_loadingState.update { false }
	}
}