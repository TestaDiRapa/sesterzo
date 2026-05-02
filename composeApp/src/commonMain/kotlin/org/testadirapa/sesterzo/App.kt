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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.testadirapa.sesterzo.components.errors.ErrorAlert
import org.testadirapa.sesterzo.screens.AuthScreen
import org.testadirapa.sesterzo.screens.CreateSpaceScreen
import org.testadirapa.sesterzo.screens.MainScreen
import org.testadirapa.sesterzo.screens.LoadingScreen
import org.testadirapa.sesterzo.screens.crypto.BackupPrivateKeyScreen
import org.testadirapa.sesterzo.screens.crypto.RestorePrivateKeyScreen
import org.testadirapa.sesterzo.styles.SesterzoTheme
import org.testadirapa.sesterzo.viewmodel.AppViewModel
import org.testadirapa.sesterzo.viewmodel.intents.AppIntent
import org.testadirapa.sesterzo.viewmodel.state.AuthenticateState
import org.testadirapa.sesterzo.viewmodel.state.BackupPrivateKeyState
import org.testadirapa.sesterzo.viewmodel.state.CreateSpaceState
import org.testadirapa.sesterzo.viewmodel.state.MainScreenState
import org.testadirapa.sesterzo.viewmodel.state.RecoverKeyState
import org.testadirapa.sesterzo.viewmodel.state.StartupState

@Composable
@Preview
fun App() {
	val appViewModel = viewModel(key = "app") { AppViewModel() }
	val state by appViewModel.appState.collectAsState()
	val errorState = appViewModel.errorState.collectAsState()
	val loadingState = appViewModel.loadingState.collectAsState()

	val widthDp = with(LocalDensity.current) {
		LocalWindowInfo.current.containerSize.width.toDp()
	}
	val isMobile = widthDp < 960.dp

	SesterzoTheme(
		darkTheme = isSystemInDarkTheme()
	) {
		Surface(
			modifier = Modifier.fillMaxSize(),
			color = MaterialTheme.colorScheme.background
		) {
			Box(modifier = Modifier.fillMaxSize()) {
				when (val currentState = state) {
					StartupState -> LoadingScreen()
					is AuthenticateState -> AuthScreen(
						isLoading = loadingState.value,
						isMobile = isMobile,
						onStartRegistration = { email, name ->
							appViewModel.acceptIntent(
								AppIntent.StartRegistration(email, name)
							)
						},
						onStartLogin = { email ->
							appViewModel.acceptIntent(
								AppIntent.StartLogin(email)
							)
						},
						onCompleteAuth = { token ->
							appViewModel.acceptIntent(AppIntent.CompleteAuthentication(token))
						},
						captchaProgressState = currentState.captchaStateFlow
					)
					BackupPrivateKeyState -> BackupPrivateKeyScreen(
						isMobile = isMobile,
						onUserAccept = { appViewModel.acceptIntent(AppIntent.ConfirmBackup) },
						onError = { appViewModel.onError(it) }
					)
					is RecoverKeyState -> RestorePrivateKeyScreen(
						isLoading = loadingState.value,
						isMobile = isMobile,
						onRestoreWithPrivateKey = { key ->
							appViewModel.acceptIntent(AppIntent.RestoreWithPrivateKey(key))
						},
						onRestoreWithRecoveryKey = { key ->
							appViewModel.acceptIntent(AppIntent.RestoreWithRecoveryKey(key))
						}
					)
					is CreateSpaceState -> CreateSpaceScreen(
						isMobile = isMobile,
						currentSpace = currentState.currentSpace,
						isLoading = loadingState.value,
						onError = { appViewModel.onError(it) },
						onCreateSpace = { name, picture, color ->
							appViewModel.acceptIntent(AppIntent.CreateSpaceIntent(name, picture, color))
						},
						onCancel = {
							appViewModel.acceptIntent(AppIntent.NavigateToMainScreen(it))
						}
					)
					is MainScreenState -> MainScreen(
						isMobile = isMobile,
						initialSpace = currentState.initialSpace,
						onError = appViewModel::onError,
						onCreateSpace = {
							appViewModel.acceptIntent(AppIntent.NavigateToSpaceCreationIntent(it))
						}
					)
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