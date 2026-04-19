package org.testadirapa.sesterzo.screens

import androidx.compose.runtime.Composable
import org.testadirapa.sesterzo.screens.main.mobile.MobileMainScreen

@Composable
fun MainScreen(
	isMobile: Boolean,
	initialSpaceId: String
) {
	if (isMobile) {
		MobileMainScreen(
			initialSpaceId = initialSpaceId,
		)
	} else {
		TODO()
	}
}