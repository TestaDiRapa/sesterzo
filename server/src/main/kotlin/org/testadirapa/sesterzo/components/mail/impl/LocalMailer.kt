package org.testadirapa.sesterzo.components.mail.impl

import org.testadirapa.sesterzo.components.mail.Mailer

class LocalMailer : Mailer {

	/**
	 * Last sent token by email.
	 */
	val lastSentToken: MutableMap<String, String> = mutableMapOf()

	override suspend fun sendAccessTokenEmail(email: String, name: String, token: String) {
		lastSentToken[email] = token
	}

	override suspend fun sendRegistrationEmail(email: String, name: String, token: String) {
		lastSentToken[email] = token
	}

	fun getLastToken(email: String): String? = lastSentToken[email]

}