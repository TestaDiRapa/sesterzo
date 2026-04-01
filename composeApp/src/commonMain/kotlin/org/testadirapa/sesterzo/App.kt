package org.testadirapa.sesterzo

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import org.testadirapa.sesterzo.pages.register.RegistrationPage
import org.testadirapa.sesterzo.styles.SesterzoTheme

@Composable
@Preview
fun App() {
	val appViewModel = viewModel { AppViewModel() }
	val state by appViewModel.appState.collectAsState()

	SesterzoTheme(
		darkTheme = isSystemInDarkTheme()
	) {
		Surface(
			modifier = Modifier.fillMaxSize(),
			color = MaterialTheme.colorScheme.background
		) {
			when (state) {
				AppState.Startup -> {}
				AppState.Authenticate -> RegistrationPage()
			}
		}
	}
}