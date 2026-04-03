package org.testadirapa.sesterzo.components.mail.impl

import io.ktor.util.logging.Logger
import org.testadirapa.sesterzo.components.mail.Mailer

class LocalMailer(
	private val logger: Logger
) : Mailer {

	/**
	 * Last sent token by email.
	 */
	val lastSentToken: MutableMap<String, String> = mutableMapOf()

	override suspend fun sendAccessTokenEmail(email: String, name: String, token: String) {
		logger.info("Login token for $email: $token")
		lastSentToken[email] = token
	}

	override suspend fun sendRegistrationEmail(email: String, name: String, token: String) {
		logger.info("Registration token for $email: $token")
		lastSentToken[email] = token
	}

	fun getLastToken(email: String): String? = lastSentToken[email]

}