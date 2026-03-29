package org.testadirapa.sesterzo.api.processes

import org.testadirapa.sesterzo.api.AuthApi
import org.testadirapa.sesterzo.model.dto.AuthResponse

class LoginProcess(
	val email: String,
	baseUrl: String
) : Process(baseUrl = baseUrl) {

	override suspend fun completeProcess(
		authApi: AuthApi,
		token: String
	): AuthResponse = authApi.login(email, token).bodyOrThrow()


}