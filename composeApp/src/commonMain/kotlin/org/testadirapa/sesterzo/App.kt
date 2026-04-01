package org.testadirapa.sesterzo

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.icure.kryptom.crypto.defaultCryptoService
import kotlinx.coroutines.launch
import org.testadirapa.sesterzo.config.PlatformContext
import org.testadirapa.sesterzo.security.authenticateUsingBiometric
import org.testadirapa.sesterzo.storage.StorageFacade
import org.testadirapa.sesterzo.styles.SesterzoTheme

@Composable
@Preview
fun App() {
	val appViewModel = viewModel { AppViewModel() }

	SesterzoTheme(
		darkTheme = isSystemInDarkTheme()
	) {
		val scope = rememberCoroutineScope()
		var storage by remember { mutableStateOf<StorageFacade?>(null) }
		var isAuthenticated by remember { mutableStateOf(true) }
		var content by remember { mutableStateOf<String?>(null) }
		if (isAuthenticated) {
			LaunchedEffect("storage") {
				storage = PlatformContext.storageFacade()
			}
			Column(
				modifier = Modifier
					.background(MaterialTheme.colorScheme.primaryContainer)
					.safeContentPadding()
					.fillMaxSize(),
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				Button(onClick = {
					scope.launch {
						storage?.setItem("test", defaultCryptoService.strongRandom.randomUUID())
					}
				}) {
					Text("Store something")
				}
				Button(onClick = {
					scope.launch {
						content = storage?.getItem("test")
					}
				}) {
					Text("Retrieve something")
				}
				Text("Storage is null ${storage == null}")
				Text("Retrieve: $content")
			}
		}
	}
}