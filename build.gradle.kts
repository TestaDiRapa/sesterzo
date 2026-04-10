plugins {
	alias(libs.plugins.androidApplication) apply false
	alias(libs.plugins.androidLibrary) apply false
	alias(libs.plugins.composeMultiplatform) apply false
	alias(libs.plugins.composeCompiler) apply false
	alias(libs.plugins.kotlinJvm) apply false
	alias(libs.plugins.kotlinMultiplatform) apply false
	alias(libs.plugins.ktor) apply false
	alias(libs.plugins.kotlinAndroid) apply false
	alias(libs.plugins.androidMultiplatformLibrary) apply false
	alias(libs.plugins.buildkonfig) apply false
	alias(libs.plugins.sqlDelight) apply false
}

buildscript {
	dependencies {
		// For KGP
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.20")
	}
	repositories {
		mavenCentral()
	}
}