package org.testadirapa.sesterzo.exceptions

import org.testadirapa.sesterzo.viewmodel.state.AppState

class UnexpectedStateException(state: AppState) : RuntimeException("Unexpected state ${state::class.simpleName}")