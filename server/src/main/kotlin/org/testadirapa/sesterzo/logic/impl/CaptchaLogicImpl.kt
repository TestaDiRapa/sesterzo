package org.testadirapa.sesterzo.logic.impl

import com.github.benmanes.caffeine.cache.Caffeine
import com.icure.kerberus.Challenge
import com.icure.kerberus.Solution
import com.icure.kerberus.validateSolution
import com.icure.kryptom.crypto.defaultCryptoService
import org.testadirapa.sesterzo.logic.CaptchaLogic
import org.testadirapa.sesterzo.model.dto.CaptchaChallenge
import java.util.concurrent.TimeUnit

class CaptchaLogicImpl(
	val numSalts: Int,
	val difficultyFactor: Int
): CaptchaLogic {

	val challengeCache = Caffeine.newBuilder()
		.expireAfterWrite(5, TimeUnit.MINUTES)
		.build<String, CaptchaChallenge>()

	override fun generateChallenge(input: String): CaptchaChallenge =
		CaptchaChallenge(
			input = input,
			challenge = Challenge(
				id = defaultCryptoService.strongRandom.randomUUID(),
				salts = List(numSalts) { defaultCryptoService.strongRandom.randomUUID() },
				difficultyFactor = difficultyFactor
			)
		).also { challengeCache.put(it.challenge.id, it) }

	override suspend fun validateChallenge(solution: Solution): Boolean =
		challengeCache.getIfPresent(solution.id)?.let {
			validateSolution(config = it.challenge, result = solution, serializedInput = it.input)
		} ?: false
}