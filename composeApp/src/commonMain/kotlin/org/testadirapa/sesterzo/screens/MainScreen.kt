package org.testadirapa.sesterzo.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.screens.main.mobile.MobileMainScreen

@Composable
fun MainScreen(
	isMobile: Boolean,
	initialSpace: Space,
	onError: (e: Throwable) -> Unit,
	onCreateSpace: (Space) -> Unit,
) {
	LaunchedEffect(Unit) {
		AppCtx.currency = AppCtx.api.user.getCurrentUser().preferredCurrency
	}
	if (isMobile) {
		MobileMainScreen(
			initialSpace = initialSpace,
			onError = onError,
			onCreateSpace = onCreateSpace,
		)
	} else {
		TODO()
	}
}