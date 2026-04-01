package org.testadirapa.sesterzo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import org.testadirapa.sesterzo.config.PlatformContext

class MainActivity : FragmentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		enableEdgeToEdge()
		super.onCreate(savedInstanceState)

		setContent {
			PlatformContext.setup(
				application = this.application,
				fragmentActivityContext = this,
				unlockStorageTitle = "Tap to unlock your budget"
			)
			App()
		}
	}
}

@Preview
@Composable
fun AppAndroidPreview() {
	App()
}