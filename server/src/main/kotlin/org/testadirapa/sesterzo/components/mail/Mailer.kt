package org.testadirapa.sesterzo.components.mail

interface Mailer {

	/**
	 * Sends an email containing a short, temporary access token for login.
	 *
	 * @param email the email.
	 * @param name the name of the user, for templating purposes.
	 * @param token the token.
	 */
	suspend fun sendAccessTokenEmail(email: String, name: String, token: String)

	/**
	 * Sends an email containing a short, temporary access token for the first-time registration.
	 *
	 * @param email the email.
	 * @param name the name of the user, for templating purposes.
	 * @param token the token.
	 */
	suspend fun sendRegistrationEmail(email: String, name: String, token: String)
}