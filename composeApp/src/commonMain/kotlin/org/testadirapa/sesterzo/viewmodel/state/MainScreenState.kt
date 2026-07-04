package org.testadirapa.sesterzo.viewmodel.state

import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.screens.main.mobile.Page

data class MainScreenState(
	val initialSpace: Space,
	val initialPage: Page,
) : AppState