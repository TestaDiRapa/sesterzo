package org.testadirapa.sesterzo.components.security

/**
 * Functional interface that defines the method to hash a token and verify the hash.
 */
interface PasswordEncoder {
	/**
	 * Hashes a token.
	 *
	 * @param token the token to hash.
	 * @return the generated hash.
	 */
	fun hashToken(token: String): String

	/**
	 * Verifies a plain-text token against a hash.
	 *
	 * @param token the plain-text token to verify.
	 * @param hash the hash to verify against.
	 * @return true if the hash corresponds to the token, false otherwise
	 */
	fun checkHash(
		token: String,
		hash: String,
	): Boolean

	/**
	 * @param maybeHashed a [String].
	 * @return true if [maybeHashed] is a hash string created with this encoder, false otherwise.
	 */
	fun isHashed(maybeHashed: String): Boolean
}