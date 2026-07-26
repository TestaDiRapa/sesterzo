import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

val localProps = Properties().apply {
	val localPropsFile = rootProject.file("local.properties")
	if (localPropsFile.exists()) {
		localPropsFile.inputStream().use { load(it) }
	}
}

// Signing credentials come from local.properties (which is gitignored) and fall back to
// environment variables so CI and the Docker build can inject them.
fun signingProp(key: String, env: String): String? =
	localProps.getProperty(key) ?: System.getenv(env)

plugins {
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.composeMultiplatform)
	alias(libs.plugins.composeCompiler)
}

dependencies {
	implementation(projects.composeApp)
	implementation(projects.shared)
	implementation(libs.compose.uiToolingPreview)
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.core.splashscreen)
	implementation(libs.androidx.fragment)
}

android {
	namespace = "org.testadirapa.sesterzo.android"
	compileSdk = libs.versions.android.compileSdk.get().toInt()

	defaultConfig {
		applicationId = "org.testadirapa.sesterzo"
		minSdk = libs.versions.android.minSdk.get().toInt()
		targetSdk = libs.versions.android.targetSdk.get().toInt()
		versionCode = 1
		versionName = "1.0.0"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
	signingConfigs {
		create("release") {
			signingProp("release.keystore.path", "RELEASE_KEYSTORE_PATH")?.let { path ->
				storeFile = rootProject.file(path)
				storePassword = signingProp("release.keystore.password", "RELEASE_KEYSTORE_PASSWORD")
				keyAlias = signingProp("release.key.alias", "RELEASE_KEY_ALIAS")
				keyPassword = signingProp("release.key.password", "RELEASE_KEY_PASSWORD")
			}
		}
	}
	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
			// Sign only when the keystore is configured; without it the build still
			// produces the (uninstallable) unsigned APK instead of failing outright.
			// findByName, not getByName: F-Droid's builder strips the signingConfigs
			// block entirely, and this must stay valid when the config is absent.
			signingConfig = signingConfigs.findByName("release")?.takeIf { it.storeFile != null }
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlin {
		compilerOptions {
			jvmTarget = JvmTarget.JVM_11
		}
	}
}