package org.testadirapa.sesterzo.logic

import org.testadirapa.sesterzo.model.dto.AuthResponse

interface AuthenticationLogic {

	data class RegistrationProcess(
		val id: String,
		val email: String,
		val name: String,
		val token: String
	)

	/**
	 * Starts a new registration process, saving temporarily the email and name of the user in a cache for a limited time
	 * interval.
	 * It generates a single use token that is sent to the user and will be used in validation.
	 *
	 * @param email the email of the user.
	 * @param name the name of the user.
	 * @return the id of the [RegistrationProcess].
	 */
	suspend fun startRegistration(email: String, name: String): String

	/**
	 * Completes the registration process by verifying the token against the cached data. If the verification is
	 * successful, the user is created and an authentication token is returned.
	 *
	 * @param processId the id of the process.
	 * @param token the token to validate.
	 * @return an [AuthResponse], if the process is successful.
	 */
	suspend fun completeRegistration(processId: String, token: String): AuthResponse
}