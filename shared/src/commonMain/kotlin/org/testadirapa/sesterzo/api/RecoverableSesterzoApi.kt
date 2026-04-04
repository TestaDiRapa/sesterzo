package org.testadirapa.sesterzo.api

import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.services.AuthService

class RecoverableSesterzoApiImpl (
	private val httpConfig: HttpConfig,
	override val authService: AuthService,
	override val user: UserApi
) : RecoverableSesterzoApi {
}