package org.testadirapa.sesterzo.screens

import androidx.compose.runtime.Composable
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.screens.main.mobile.MobileMainScreen

@Composable
fun MainScreen(
	isMobile: Boolean,
	initialSpace: Space,
	onError: (e: Throwable) -> Unit,
) {
	if (isMobile) {
		MobileMainScreen(
			initialSpace = initialSpace,
			onError = onError
		)
	} else {
		TODO()
	}
}