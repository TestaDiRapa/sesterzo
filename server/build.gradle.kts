plugins {
	alias(libs.plugins.kotlinJvm)
	alias(libs.plugins.kotlinSerialization)
	alias(libs.plugins.ktor)
	application
}

group = "org.testadirapa.sesterzo"
version = "1.0.0"
application {
	mainClass.set("org.testadirapa.sesterzo.ApplicationKt")

	val isDevelopment: Boolean = project.ext.has("development")
	applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
	implementation(projects.shared)
	implementation(libs.logback)

	implementation(libs.ktor.serverCore)
	implementation(libs.ktor.serverCors)
	implementation(libs.ktor.serverNetty)
	implementation(libs.ktor.serverAuthJwt)
	implementation(libs.ktor.serverRateLimiting)
	implementation(libs.ktor.serverStatusPages)
	implementation(libs.ktor.serverContentNegtiation)
	implementation(libs.ktor.serverDoubleReceive)

	implementation(libs.koin.ktor)
	implementation(libs.koin.logger)

	implementation(libs.mongo.client.coroutine)
	implementation(libs.mongo.client.serialization)

	implementation(libs.jbcrypt)
	implementation(libs.caffeine)

	// Force patched versions of vulnerable transitive dependencies
	implementation(platform(libs.netty.bom))
	implementation(platform(libs.jackson.bom))
	constraints {
		implementation(libs.guava) { because("CVE-2026-102554") }
	}

	testImplementation(libs.ktor.serverTestHost)
	testImplementation(libs.kotlin.testJunit)
}