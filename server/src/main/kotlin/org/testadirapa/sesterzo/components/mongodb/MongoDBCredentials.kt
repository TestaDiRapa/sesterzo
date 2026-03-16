package org.testadirapa.sesterzo.components.mongodb

import io.ktor.server.config.*

data class MongoDBCredentials(
	val username: String,
	val password: String,
	val ip: String,
	val port: String,
	val databaseName: String,
) {
	companion object {
		fun fromConfig(config: ApplicationConfig) =
			MongoDBCredentials(
				username = config.property("mongodb.username").getString(),
				password = config.property("mongodb.password").getString(),
				ip = config.property("mongodb.ip").getString(),
				port = config.property("mongodb.port").getString(),
				databaseName = config.property("mongodb.databaseName").getString(),
			)
	}

	fun toConnectionString() = "mongodb://$username:$password@$ip:$port/$databaseName"
}