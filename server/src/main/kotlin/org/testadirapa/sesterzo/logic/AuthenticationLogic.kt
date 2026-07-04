package org.testadirapa.sesterzo.logic

import com.icure.kerberus.Solution
import org.testadirapa.sesterzo.exceptions.InvalidCaptchaException
import org.testadirapa.sesterzo.model.dto.AuthResponse
import org.testadirapa.sesterzo.exceptions.UnauthorizedException

interface AuthenticationLogic {

	data class RegistrationProcess(
		val id: String,
		val email: String,
		val name: String,
		val logsOptIn: Boolean,
		val token: String
	)

	/**
	 * Starts a new registration process, saving temporarily the email and name of the user in a cache for a limited time
	 * interval.
	 * It generates a single use token that is sent to the user and will be used in validation.
	 *
	 * @param email the email of the user.
	 * @param name the name of the user.
	 * @param solution the captcha [Solution].
	 * @param logsOptIn whether the user accepts to send anonymous logs in case of error.
	 * @return the id of the [RegistrationProcess].
	 * @throws [InvalidCaptchaException] if the provided captcha solution is not valid.
	 */
	suspend fun startRegistration(email: String, name: String, logsOptIn: Boolean, solution: Solution): String

	/**
	 * Completes the registration process by verifying the token against the cached data. If the verification is
	 * successful, the user is created and an authentication token is returned.
	 *
	 * @param processId the id of the process.
	 * @param token the token to validate.
	 * @return an [AuthResponse], if the process is successful.
	 */
	suspend fun completeRegistration(processId: String, token: String): AuthResponse

	/**
	 * Generates a new OTT for the user with the specified [email] and sends it via mail. The token is saved in a cache
	 * and is valid for a single access, in a time window of 15 minutes. Requesting another token will automatically
	 * invalidate the previous one.
	 * If a user with the specified email does not exist, no error is returned.
	 *
	 * @param email the email of the user.
	 * @param solution a captcha [Solution].
	 */
	suspend fun generateOTT(email: String, solution: Solution)

	/**
	 * Logs in a user by email and token.
	 * @param email the user email.
	 * @param token the user token.
	 * @return an [AuthResponse] if the process is successful.
	 * @throws [UnauthorizedException] if the process fails for any reason.
	 */
	suspend fun login(email: String, token: String): AuthResponse

	/**
	 * Generates a new JWT for a user.
	 * @param userId the id of the user.
	 * @param refreshToken the refresh token, already validated.
	 * @return an [AuthResponse] where [AuthResponse.refreshJwt] is the refresh token passed as parameter.
	 */
	suspend fun refresh(userId: String, refreshToken: String): AuthResponse
}