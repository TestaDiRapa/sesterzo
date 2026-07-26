import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import java.util.Properties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidMultiplatformLibrary)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
	alias(libs.plugins.buildkonfig)
}

val localProps = Properties().apply {
	val localPropsFile = rootProject.file("local.properties")
	if (localPropsFile.exists()) {
		localPropsFile.inputStream().use { load(it) }
	}
}

buildkonfig {
	packageName = "org.testadirapa.sesterzo"

	defaultConfigs {
		buildConfigField(STRING, "apiUrl", localProps.getProperty("api.url"))
		buildConfigField(INT, "cacheTtl", localProps.getProperty("api.cache.ttl"))
		buildConfigField(INT, "spaceLimit", localProps.getProperty("space.limit"))
	}
}


kotlin {

	android {
		namespace = "org.testadirapa.sesterzo.compose"
		compileSdk = libs.versions.android.compileSdk.get().toInt()

		compilerOptions {
			jvmTarget.set(JvmTarget.JVM_11)
		}

		androidResources {
			enable = true
		}
	}

	js {
		browser()
		binaries.executable()
	}

//	@OptIn(ExperimentalWasmDsl::class)
//	wasmJs {
//		browser()
//		binaries.executable()
//	}

	sourceSets {
		commonMain.dependencies {
			implementation(projects.shared)
			implementation(libs.kotlinx.datetime)
			implementation(libs.compose.runtime)
			implementation(libs.compose.foundation)
			implementation(libs.compose.material3)
			implementation(libs.compose.ui)
			implementation(libs.compose.components.resources)
			implementation(libs.compose.uiToolingPreview)
			implementation(libs.androidx.lifecycle.viewmodelCompose)
			implementation(libs.androidx.lifecycle.runtimeCompose)
			implementation(libs.kermit)
			implementation(libs.qrose)
		}
		androidMain.dependencies {
			implementation(libs.androidx.activity.compose)
			implementation(libs.androidx.camera.core)
			implementation(libs.androidx.camera.camera2)
			implementation(libs.androidx.camera.lifecycle)
			implementation(libs.androidx.camera.view)
			implementation(libs.zxingcpp)
		}
		commonTest.dependencies {
			implementation(libs.kotlin.test)
		}
	}
}

//dependencies {
//	androidRuntimeClasspath(libs.compose.uiTooling)
//}

// Keep the production JS distribution small: no source maps shipped to the browser.
tasks.named<KotlinWebpack>("jsBrowserProductionWebpack") {
	sourceMaps = false
}

// The emscripten glue and the skiko wasm are already bundled into composeApp.js (webpack
// emits the wasm as a content-hashed asset), so the standalone copies pulled in from the
// skiko klib resources are never fetched at runtime - ~9.6 MB of dead weight.
tasks.named<Sync>("jsBrowserDistribution") {
	exclude("skiko.mjs", "skikod8.mjs", "skiko.wasm")
}

