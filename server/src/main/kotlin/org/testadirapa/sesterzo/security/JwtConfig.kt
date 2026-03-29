package org.testadirapa.sesterzo.security

import io.ktor.server.config.ApplicationConfig
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

class JwtConfig(
	val authPrivateKey: RSAPrivateKey,
	val authPublicKey: RSAPublicKey,
	val refreshPrivateKey: RSAPrivateKey,
	val refreshPublicKey: RSAPublicKey,
	val issuer: String,
	val audience: String,
	val realm: String,
) {
	companion object {
		fun fromConfig(config: ApplicationConfig) =
			JwtConfig(
				authPrivateKey = loadPrivateKey(config.property("ktor.jwt.authPrivateKey").getString()),
				authPublicKey = loadPublicKey(config.property("ktor.jwt.authPublicKey").getString()),
				refreshPrivateKey = loadPrivateKey(config.property("ktor.jwt.refreshPrivateKey").getString()),
				refreshPublicKey = loadPublicKey(config.property("ktor.jwt.refreshPublicKey").getString()),
				issuer = config.property("ktor.jwt.issuer").getString(),
				audience = config.property("ktor.jwt.audience").getString(),
				realm = config.property("ktor.jwt.realm").getString(),
			)

		private fun loadPrivateKey(pem: String): RSAPrivateKey {
			val keyBytes = pem
				.replace("-----BEGIN PRIVATE KEY-----", "")
				.replace("-----END PRIVATE KEY-----", "")
				.replace("\\s".toRegex(), "")
				.let { Base64.getDecoder().decode(it) }
			return KeyFactory.getInstance("RSA")
				.generatePrivate(PKCS8EncodedKeySpec(keyBytes)) as RSAPrivateKey
		}

		private fun loadPublicKey(pem: String): RSAPublicKey {
			val keyBytes = pem
				.replace("-----BEGIN PUBLIC KEY-----", "")
				.replace("-----END PUBLIC KEY-----", "")
				.replace("\\s".toRegex(), "")
				.let { Base64.getDecoder().decode(it) }
			return KeyFactory.getInstance("RSA")
				.generatePublic(X509EncodedKeySpec(keyBytes)) as RSAPublicKey
		}
	}
}