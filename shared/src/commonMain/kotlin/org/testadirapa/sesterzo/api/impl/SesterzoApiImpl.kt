package org.testadirapa.sesterzo.api.impl

import io.ktor.client.HttpClient
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.services.AuthService

class SesterzoApiImpl(
	private val httpConfig: HttpConfig,
	val authService: AuthService
) {

}