package org.testadirapa.sesterzo.api.impl

import org.testadirapa.sesterzo.api.SesterzoApi
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.services.AuthService

class SesterzoApiImpl(
	private val httpConfig: HttpConfig,
	val authService: AuthService
) : SesterzoApi {

}