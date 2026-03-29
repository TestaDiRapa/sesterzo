package org.testadirapa.sesterzo.api

import com.icure.kerberus.Solution
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.model.dto.AuthResponse
import org.testadirapa.sesterzo.model.dto.CaptchaChallenge
import org.testadirapa.sesterzo.model.dto.StartRegistrationData

interface AuthApi {

	/**
	 * Get a new [CaptchaChallenge] for the specified input.
	 *
	 * @param input a random secret. Needs to be provided along with the captcha solution.
	 * @return an [HttpResponse] of [CaptchaChallenge].
	 */
	suspend fun getCaptchaChallenge(input: String): HttpResponse<CaptchaChallenge>

	/**
	 * Starts a new registration process.
	 * This process requires a solved captcha form [getCaptchaChallenge].
	 *
	 * @param registrationData the information needed for the registration.
	 * @return the id for the registration process.
	 */
	suspend fun startRegistration(registrationData: StartRegistrationData): HttpResponse<String>

	/**
	 * Completes the registration and, if the process is successful, returns valid access tokens for the user.
	 *
	 * @param registrationProcessId the id of the registration process.
	 * @param token the token received by email.
	 * @return an [HttpResponse] of [AuthResponse].
	 */
	suspend fun completeRegistration(registrationProcessId: String, token: String): HttpResponse<AuthResponse>

	/**
	 * Asks for a new OTT to be sent to the specified email.
	 *
	 * @param email the email where to send the token.
	 * @param captchaSolution the solution of the captcha provided by [getCaptchaChallenge].
	 */
	suspend fun getOtt(email: String, captchaSolution: Solution): HttpResponse<Unit>

	/**
	 * Logs in a user returning the access tokens if the process is successful.
	 *
	 * @param email the user email.
	 * @param token a valid token.
	 * @return an [HttpResponse] of [AuthResponse].
	 */
	suspend fun login(email: String, token: String): HttpResponse<AuthResponse>

	/**
	 * Refreshes the authentication token of a user through the refresh token.
	 * Note: the [AuthResponse.refreshJwt] in the response is the same passed in the request.
	 *
	 * @return an [HttpResponse] of [AuthResponse].
	 */
	suspend fun refresh(refreshToken: String): HttpResponse<AuthResponse>
}