import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlinMultiplatform)
//	alias(libs.plugins.androidMultiplatformLibrary)
}

kotlin {

//	androidLibrary {
//		namespace = "org.testadirapa.sesterzo.compose"
//		compileSdk = libs.versions.android.compileSdk.get().toInt()
//
//		compilerOptions {
//			jvmTarget.set(JvmTarget.JVM_11)
//		}
//
//		androidResources {
//			enable = true
//		}
//	}

	jvm()

	js {
		browser()
	}

	@OptIn(ExperimentalWasmDsl::class)
	wasmJs {
		browser()
	}

	sourceSets {
		commonMain.dependencies {
			implementation(libs.kotlinx.serialization.json)
			api(libs.kryptom)
			api(libs.kerberus)
		}
		commonTest.dependencies {
			implementation(libs.kotlin.test)
		}
	}
}
