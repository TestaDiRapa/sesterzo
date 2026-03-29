import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidMultiplatformLibrary)
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

	jvm()

	js {
		browser()
	}

//	@OptIn(ExperimentalWasmDsl::class)
//	wasmJs {
//		browser()
//	}

	sourceSets {
		commonMain.dependencies {
			implementation(libs.kotlinx.serialization.json)
			api(libs.kryptom)
			api(libs.kerberus)

			api(libs.ktor.clientCore)
			api(libs.ktor.clientContentNegotiation)
			api(libs.ktor.serialization)
		}
		jvmMain.dependencies {
			api(libs.ktor.clientOkHttp)
		}
		commonTest.dependencies {
			implementation(libs.kotlin.test)
		}
		webMain.dependencies {
			api(libs.ktor.clientJs)
		}
		androidMain.dependencies {
			api(libs.ktor.clientAndroid)
		}
	}
}
