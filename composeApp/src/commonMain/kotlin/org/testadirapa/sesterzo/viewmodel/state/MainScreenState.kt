package org.testadirapa.sesterzo.viewmodel.state

import org.testadirapa.sesterzo.model.Space

data class MainScreenState(
	val initialSpace: Space
) : AppState