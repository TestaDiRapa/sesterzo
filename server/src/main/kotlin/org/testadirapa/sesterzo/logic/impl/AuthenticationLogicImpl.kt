package org.testadirapa.sesterzo.logic.impl

import com.github.benmanes.caffeine.cache.Caffeine
import com.icure.kryptom.crypto.defaultCryptoService
import kotlinx.coroutines.flow.mapNotNull
import org.testadirapa.sesterzo.components.mail.Mailer
import org.testadirapa.sesterzo.dao.SpaceDAO
import org.testadirapa.sesterzo.dao.UserDAO
import org.testadirapa.sesterzo.exceptions.InvalidRegistrationException
import org.testadirapa.sesterzo.logic.AuthenticationLogic
import org.testadirapa.sesterzo.model.User
import org.testadirapa.sesterzo.model.dto.AuthResponse
import org.testadirapa.sesterzo.security.JWTClaims
import org.testadirapa.sesterzo.security.JWTManager
import org.testadirapa.sesterzo.security.JWTRefreshClaims
import org.testadirapa.sesterzo.utils.toMap
import java.util.concurrent.TimeUnit

class AuthenticationLogicImpl(
	private val tokenLength: Int,
	private val mailer: Mailer,
	private val userDAO: UserDAO,
	private val spaceDAO: SpaceDAO,
	private val jwtManager: JWTManager,
) : AuthenticationLogic {

	val registrationCache = Caffeine.newBuilder()
		.expireAfterWrite(15, TimeUnit.MINUTES)
		.build<String, AuthenticationLogic.RegistrationProcess>()

	private fun generateShortToken(length: Int): String =
		defaultCryptoService.strongRandom.randomBytes(length).joinToString("") {
			"${it.toShort() % 10}"
		}

	private suspend fun buildAuthResponse(userId: String): AuthResponse {
		val spacesWithPermission = spaceDAO.getByParticipant(userId).mapNotNull { space ->
			space.users[userId]?.let {
				space.id to it
 			}
		}.toMap()
		return AuthResponse(
			jwt = jwtManager.generateAuthJWT(JWTClaims(userId = userId, spaces = spacesWithPermission)),
			refreshJwt = jwtManager.generateRefreshJWT(JWTRefreshClaims(userId)),
		)
	}

	override suspend fun startRegistration(email: String, name: String): String {
		val token = generateShortToken(tokenLength)
		val processId = defaultCryptoService.strongRandom.randomUUID()
		registrationCache.put(
			processId,
			AuthenticationLogic.RegistrationProcess(processId, email, name, token)
		)
		mailer.sendRegistrationEmail(email, name, token)
		return processId
	}

	override suspend fun completeRegistration(processId: String, token: String): AuthResponse {
		val process = registrationCache.getIfPresent(processId)
			?: throw InvalidRegistrationException(processId, token)
		if (process.token != token) {
			throw InvalidRegistrationException(processId, token)
		}
		registrationCache.invalidate(processId)
		val createdUserId = userDao.save(
			User(
				id = defaultCryptoService.strongRandom.randomUUID(),
				email = process.email,
				name = process.name
			)
		)
		return buildAuthResponse(createdUserId)
	}
}