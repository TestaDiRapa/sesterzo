package org.testadirapa.sesterzo.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Payload
import io.ktor.server.auth.jwt.*
import org.testadirapa.sesterzo.exceptions.JWTException
import org.testadirapa.sesterzo.exceptions.UnauthorizedException
import org.testadirapa.sesterzo.model.UserSpaceRole
import org.testadirapa.sesterzo.security.UserJwtClaims.Companion.SPACES_KEY
import org.testadirapa.sesterzo.security.UserJwtClaims.Companion.USER_ID_KEY
import java.util.*
import kotlin.collections.mapValues
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

/**
 * This component manages the authentication and refresh JWT creation and verification
 */
class JwtManager(
	val config: JwtConfig,
) {
	companion object {
		private val authJWTDuration = 15.minutes.inWholeMilliseconds
		private val refreshJWTDuration = 30.days.inWholeMilliseconds
	}

	/**
	 * Generates an authentication JWT from the claims passed as parameter.
	 * The duration of the token is set to 1 hour.
	 *
	 * @param jwtClaims the [JwtClaims] to put in the token.
	 * @return a base64-encoded JWT.
	 */
	fun generateAuthJWT(jwtClaims: JwtClaims): String =
		JWT.create()
			.withAudience(config.audience)
			.withIssuer(config.issuer)
			.withClaim(USER_ID_KEY, jwtClaims.userId)
			.withClaim(SPACES_KEY, jwtClaims.spaces)
			.withExpiresAt(Date(System.currentTimeMillis() + authJWTDuration))
			.sign(Algorithm.RSA256(config.authPublicKey, config.authPrivateKey))

	/**
	 * Generates a refresh JWT from the claims passed as parameter.
	 * The duration of the token is set to 1 hour.
	 *
	 * @param jwtClaims the [JwtRefreshClaims] to put in the token.
	 * @return a base64-encoded JWT.
	 */
	fun generateRefreshJWT(jwtClaims: JwtRefreshClaims): String =
		JWT.create()
			.withAudience(config.audience)
			.withIssuer(config.issuer)
			.withClaim(USER_ID_KEY, jwtClaims.userId)
			.withExpiresAt(Date(System.currentTimeMillis() + refreshJWTDuration))
			.sign(Algorithm.RSA256(config.refreshPublicKey, config.refreshPrivateKey))

	/**
	 * Converts a [JWTCredential] to a [JWTPrincipal], ensuring that the payload is in the correct format.
	 *
	 * @param credential a [JWTCredential].
	 * @return a [JWTPrincipal].
	 */
	fun credentialToPrincipal(credential: JWTCredential): JWTPrincipal =
		if (credential.payload.getClaim(USER_ID_KEY).asString() != "") {
			JWTPrincipal(credential.payload)
		} else {
			throw UnauthorizedException("Wrong JWT format")
		}

	/**
	 * @return a [JWTVerifier] for the authentication jwt.
	 */
	fun authJWTVerifier(): JWTVerifier =
		JWT
			.require(Algorithm.RSA256(config.authPublicKey, null))
			.withAudience(config.audience)
			.withIssuer(config.issuer)
			.build()

	/**
	 * @return a [JWTVerifier] for the refresh jwt.
	 */
	fun refreshJWTVerifier(): JWTVerifier =
		JWT
			.require(Algorithm.RSA256(config.refreshPublicKey, null))
			.withAudience(config.audience)
			.withIssuer(config.issuer)
			.build()

	private fun Payload.isRefreshJwtValid() = getClaim(USER_ID_KEY).asString().isNotBlank()

	/**
	 * Converts a [JWTCredential] to a [JWTPrincipal], ensuring that the payload is in the correct format.
	 *
	 * @param credential a [JWTCredential].
	 * @return a [JWTPrincipal].
	 */
	fun refreshCredentialToPrincipal(credential: JWTCredential): JWTPrincipal =
		if (credential.payload.isRefreshJwtValid()) {
			JWTPrincipal(credential.payload)
		} else {
			throw UnauthorizedException("Wrong refresh JWT format")
		}
}

/**
 * Converts a [Payload] to [JwtClaims].
 *
 * @receiver payload a [Payload].
 * @return a [JwtClaims]
 * @throws JWTException if it the JWT is in the wrong format.
 */
fun Payload.toJWTClaims(): JwtClaims =
	try {
		JwtClaims(
			userId = getClaim(USER_ID_KEY).asString(),
			spaces = getClaim(SPACES_KEY).asMap().mapValues { (_, v) ->
				v as UserSpaceRole
			}
		)
	} catch (e: Exception) {
		throw JWTException(e.message ?: "Wrong JWT format")
	}

/**
 * Converts a [Payload] to [JwtRefreshClaims].
 *
 * @receiver payload a [Payload].
 * @return a [JwtRefreshClaims]
 * @throws JWTException if the JWT is in the wrong format.
 */
fun Payload.toJWTRefreshClaims(): JwtRefreshClaims =
	try {
		JwtRefreshClaims(
			userId = getClaim(USER_ID_KEY).asString()
		)
	} catch (e: Exception) {
		throw JWTException(e.message ?: "Wrong JWT format")
	}
