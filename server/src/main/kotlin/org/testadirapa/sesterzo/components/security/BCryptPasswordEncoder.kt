package org.testadirapa.sesterzo.components.security

import org.mindrot.jbcrypt.BCrypt

/**
 * Implementation of [PasswordEncoder] based on [BCrypt]
 */
class BCryptPasswordEncoder : PasswordEncoder {
	companion object {
		private val bcryptHashPattern = Regex("^\\\$2[ayb]\\\$\\d{2}\\\$[./0-9A-Za-z]{53}$")
	}

	override fun hashToken(token: String): String = BCrypt.hashpw(token, BCrypt.gensalt())

	override fun checkHash(
		token: String,
		hash: String,
	): Boolean = BCrypt.checkpw(token, hash)

	override fun isHashed(maybeHashed: String): Boolean = bcryptHashPattern.matches(maybeHashed)
}