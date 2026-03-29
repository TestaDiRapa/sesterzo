package org.testadirapa.sesterzo.api.processes

import org.testadirapa.sesterzo.api.AuthApi
import org.testadirapa.sesterzo.model.dto.AuthResponse

class RegistrationProcess(
	val processId: String,
	baseUrl: String
): Process(baseUrl = baseUrl) {

	override suspend fun completeProcess(
		authApi: AuthApi,
		token: String
	): AuthResponse = authApi.completeRegistration(registrationProcessId = processId, token = token).bodyOrThrow()

}