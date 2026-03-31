package org.testadirapa.sesterzo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.testadirapa.sesterzo.config.PlatformConfig

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		enableEdgeToEdge()
		super.onCreate(savedInstanceState)

		setContent {
			PlatformConfig.setup(this.application)
			App()
		}
	}
}

@Preview
@Composable
fun AppAndroidPreview() {
	App()
}