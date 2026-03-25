package org.testadirapa.sesterzo.logic

import com.icure.kerberus.Solution
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.config.getAs
import org.testadirapa.sesterzo.model.dto.CaptchaChallenge

interface CaptchaLogic {

	data class Config(
		val numSalts: Int,
		val difficultyFactor: Int
	) {
		companion object {
			fun fromConfig(config: ApplicationConfig): Config =
				Config(
					numSalts = config.property("captcha.numSalts").getAs<Int>(),
					difficultyFactor = config.property("captcha.difficultyFactor").getAs<Int>()
				)
		}
	}

	/**
	 * @param input the input from the user.
	 * @return a new [CaptchaChallenge] for the user to solve, valid for 5 minutes.
	 */
	fun generateChallenge(input: String): CaptchaChallenge

	/**
	 * Checks if the provided captcha [Solution] is valid.
	 */
	suspend fun validateChallenge(solution: Solution): Boolean
}