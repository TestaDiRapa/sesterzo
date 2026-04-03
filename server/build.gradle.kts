plugins {
	alias(libs.plugins.kotlinJvm)
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

	testImplementation(libs.ktor.serverTestHost)
	testImplementation(libs.kotlin.testJunit)
}