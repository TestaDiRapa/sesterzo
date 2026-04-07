package org.testadirapa.sesterzo.viewmodel.state

import org.testadirapa.sesterzo.api.RecoverableSesterzoApi

data class RecoverKeyState(
	val api: RecoverableSesterzoApi
) : AppState
