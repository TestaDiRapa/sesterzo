package org.testadirapa.sesterzo.utils

import org.testadirapa.sesterzo.exceptions.UnexpectedStateException
import org.testadirapa.sesterzo.viewmodel.state.AppState

inline fun <reified T : AppState> expectStateAs(state: AppState, block: (state: T) -> Unit) {
	if (state is T) {
		block(state)
	} else {
		throw UnexpectedStateException(state)
	}
}