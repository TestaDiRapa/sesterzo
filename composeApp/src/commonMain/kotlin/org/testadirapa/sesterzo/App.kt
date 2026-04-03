package org.testadirapa.sesterzo

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import org.testadirapa.sesterzo.components.errors.ErrorAlert
import org.testadirapa.sesterzo.pages.register.RegistrationPage
import org.testadirapa.sesterzo.styles.SesterzoTheme
import org.testadirapa.sesterzo.viewmodel.AppViewModel
import org.testadirapa.sesterzo.viewmodel.Intent
import org.testadirapa.sesterzo.viewmodel.state.AuthenticateState
import org.testadirapa.sesterzo.viewmodel.state.MainPageState
import org.testadirapa.sesterzo.viewmodel.state.StartupState

@Composable
@Preview
fun App() {
	val appViewModel = viewModel { AppViewModel() }
	val state by appViewModel.appState.collectAsState()
	val errorState = appViewModel.errorState.collectAsState()

	SesterzoTheme(
		darkTheme = isSystemInDarkTheme()
	) {
		Surface(
			modifier = Modifier.fillMaxSize(),
			color = MaterialTheme.colorScheme.background
		) {
			Box(modifier = Modifier.fillMaxSize()) {
				when (val currentState = state) {
					StartupState -> {}
					is AuthenticateState -> RegistrationPage(
						onStartRegistration = { email, name ->
							appViewModel.acceptIntent(
								Intent.StartRegistration(email, name)
							)
						},
						onCompleteRegistration = { token ->
							appViewModel.acceptIntent(Intent.CompleteAuthentication(token))
						},
						captchaProgressState = currentState.captchaStateFlow
					)
					MainPageState -> {}
				}
				ErrorAlert(
					error = errorState.value,
					onDismiss = { appViewModel.dismissError() },
					modifier = Modifier.align(Alignment.TopCenter)
				)
			}
		}
	}
}