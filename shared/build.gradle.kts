import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidMultiplatformLibrary)
	alias(libs.plugins.kotlinSerialization)
	alias(libs.plugins.sqlDelight)
}

kotlin {

	compilerOptions {
		freeCompilerArgs.add("-Xexpect-actual-classes")
	}

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
			implementation(libs.kotlinx.datetime)
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
			implementation(libs.indexedDb)
		}
		androidMain.dependencies {
			api(libs.ktor.clientAndroid)
			implementation(libs.androidx.biometric)
			implementation(libs.androidx.datastore)
			implementation(libs.sqlDelight.android)
			implementation(libs.sqlDelight.coroutines)
		}
	}
}

sqldelight {
	databases {
		create("AppDatabase") {
			packageName.set("org.testadira.sesterzo")
			srcDirs("src/androidMain/sqlDelight")
		}
	}
}
