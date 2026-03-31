package org.testadirapa.sesterzo

import android.app.Application
import org.testadirapa.sesterzo.config.PlatformConfig

class SesterzoApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		PlatformConfig.setup(this)
	}
}