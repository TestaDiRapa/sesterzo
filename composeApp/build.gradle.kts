import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidMultiplatformLibrary)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
}

kotlin {

	androidLibrary {
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
			implementation(libs.compose.runtime)
			implementation(libs.compose.foundation)
			implementation(libs.compose.material3)
			implementation(libs.compose.ui)
			implementation(libs.compose.components.resources)
			implementation(libs.compose.uiToolingPreview)
			implementation(libs.androidx.lifecycle.viewmodelCompose)
			implementation(libs.androidx.lifecycle.runtimeCompose)
			implementation(projects.shared)
		}
		commonTest.dependencies {
			implementation(libs.kotlin.test)
		}
	}
}

//dependencies {
//	androidRuntimeClasspath(libs.compose.uiTooling)
//}

