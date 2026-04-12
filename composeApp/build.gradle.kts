import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.INT

plugins {
	alias(libs.plugins.kotlinMultiplatform)
	alias(libs.plugins.androidMultiplatformLibrary)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
	alias(libs.plugins.buildkonfig)
}

val localProps = gradleLocalProperties(rootDir, providers)

buildkonfig {
	packageName = "org.testadirapa.sesterzo"

	defaultConfigs {
		buildConfigField(STRING, "apiUrl", localProps.getProperty("api.url"))
		buildConfigField(INT, "cacheTtl", localProps.getProperty("api.cache.ttl"))
	}
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
			implementation(projects.shared)
			implementation(libs.compose.runtime)
			implementation(libs.compose.foundation)
			implementation(libs.compose.material3)
			implementation(libs.compose.ui)
			implementation(libs.compose.components.resources)
			implementation(libs.compose.uiToolingPreview)
			implementation(libs.androidx.lifecycle.viewmodelCompose)
			implementation(libs.androidx.lifecycle.runtimeCompose)
			implementation(libs.kermit)
		}
		androidMain.dependencies {
			implementation(libs.androidx.activity.compose)
		}
		commonTest.dependencies {
			implementation(libs.kotlin.test)
		}
	}
}

//dependencies {
//	androidRuntimeClasspath(libs.compose.uiTooling)
//}

