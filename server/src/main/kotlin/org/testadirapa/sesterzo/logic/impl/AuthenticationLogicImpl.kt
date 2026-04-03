package org.testadirapa.sesterzo.logic.impl

import com.github.benmanes.caffeine.cache.Caffeine
import com.icure.kerberus.Solution
import com.icure.kryptom.crypto.defaultCryptoService
import kotlinx.coroutines.flow.mapNotNull
import org.testadirapa.sesterzo.components.mail.Mailer
import org.testadirapa.sesterzo.components.security.PasswordEncoder
import org.testadirapa.sesterzo.dao.SpaceDAO
import org.testadirapa.sesterzo.dao.UserDAO
import org.testadirapa.sesterzo.exceptions.InvalidCaptchaException
import org.testadirapa.sesterzo.exceptions.InvalidRegistrationException
import org.testadirapa.sesterzo.exceptions.InvalidRegistrationParametersException
import org.testadirapa.sesterzo.exceptions.UnauthorizedException
import org.testadirapa.sesterzo.logic.AuthenticationLogic
import org.testadirapa.sesterzo.logic.CaptchaLogic
import org.testadirapa.sesterzo.model.User
import org.testadirapa.sesterzo.model.dto.AuthResponse
import org.testadirapa.sesterzo.security.JwtClaims
import org.testadirapa.sesterzo.security.JwtManager
import org.testadirapa.sesterzo.security.JwtRefreshClaims
import org.testadirapa.sesterzo.utils.toMap
import org.testadirapa.sesterzo.validators.EmailValidator
import org.testadirapa.sesterzo.validators.NotBlankValidator
import java.util.concurrent.TimeUnit

class AuthenticationLogicImpl(
	private val tokenLength: Int,
	private val mailer: Mailer,
	private val userDAO: UserDAO,
	private val spaceDAO: SpaceDAO,
	private val captchaLogic: CaptchaLogic,
	private val jwtManager: JwtManager,
	private val passwordEncoder: PasswordEncoder
) : AuthenticationLogic {

	val registrationCache = Caffeine.newBuilder()
		.expireAfterWrite(15, TimeUnit.MINUTES)
		.build<String, AuthenticationLogic.RegistrationProcess>()

	val ottCache = Caffeine.newBuilder()
		.expireAfterWrite(15, TimeUnit.MINUTES)
		.build<String, String>()

	private fun generateShortToken(length: Int): String =
		defaultCryptoService.strongRandom.randomBytes(length).joinToString("") {
			"${it.toUShort() % 10u}"
		}

	private suspend fun buildAuthResponse(userId: String, refreshToken: String? = null): AuthResponse {
		val spacesWithPermission = spaceDAO.getByParticipant(userId).mapNotNull { space ->
			space.users[userId]?.let {
				space.id to it
 			}
		}.toMap()
		return AuthResponse(
			jwt = jwtManager.generateAuthJWT(JwtClaims(userId = userId, spaces = spacesWithPermission)),
			refreshJwt = refreshToken ?: jwtManager.generateRefreshJWT(JwtRefreshClaims(userId)),
		)
	}

	private fun User.hasMatchingOtt(ott: String): Boolean? =
		ottCache.getIfPresent(email)
			?.let { it == ott }
			?.takeIf { it }
			?.also { ottCache.invalidate(email) }

	private fun User.hasMatchingToken(token: String): Boolean =
		hasMatchingOtt(token) ?: authenticationTokens.values
			.filter { it.expiresAt > System.currentTimeMillis() }
			.map { it.token }
			.any { passwordEncoder.checkHash(token = token, hash = it) }

	override suspend fun startRegistration(email: String, name: String, solution: Solution): String {
		if (!captchaLogic.validateChallenge(solution)) {
			throw InvalidCaptchaException()
		}
		if (!EmailValidator.isValid(email) || !NotBlankValidator.isValid(name)) {
			throw InvalidRegistrationParametersException()
		}
		val token = generateShortToken(tokenLength)
		val processId = defaultCryptoService.strongRandom.randomUUID()
		registrationCache.put(
			processId,
			AuthenticationLogic.RegistrationProcess(processId, email.trim(), name.trim(), token)
		)
		mailer.sendRegistrationEmail(email.trim(), name.trim(), token)
		return processId
	}

	override suspend fun completeRegistration(processId: String, token: String): AuthResponse {
		val process = registrationCache.getIfPresent(processId)
			?: throw InvalidRegistrationException(processId, token)
		if (process.token != token) {
			throw InvalidRegistrationException(processId, token)
		}
		registrationCache.invalidate(processId)
		val createdUserId = userDAO.save(
			User(
				id = defaultCryptoService.strongRandom.randomUUID(),
				email = process.email,
				name = process.name
			)
		)
		return buildAuthResponse(createdUserId)
	}

	override suspend fun generateOTT(email: String, solution: Solution) {
		if (!captchaLogic.validateChallenge(solution)) {
			throw InvalidCaptchaException()
		}
		val user = userDAO.getByEmail(email)
		if (user != null) {
			val token = ottCache.get(email) { generateShortToken(tokenLength) }
			mailer.sendAccessTokenEmail(email = email, name = user.name, token = token)
		}
	}

	override suspend fun login(email: String, token: String): AuthResponse {
		val user = userDAO.getByEmail(email) ?: throw UnauthorizedException("Invalid username or password")
		return if (user.hasMatchingToken(token)) {
			buildAuthResponse(user.id)
		} else {
			throw UnauthorizedException("Invalid username or password")
		}
	}

	override suspend fun refresh(userId: String, refreshToken: String): AuthResponse {
		userDAO.getById(userId)
			?: throw UnauthorizedException("User $userId not found")
		return buildAuthResponse(userId = userId, refreshToken = refreshToken)
	}
}