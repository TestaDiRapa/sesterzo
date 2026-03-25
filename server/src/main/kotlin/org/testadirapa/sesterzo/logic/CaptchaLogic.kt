package org.testadirapa.sesterzo.logic

import com.icure.kerberus.Solution
import org.testadirapa.sesterzo.model.dto.CaptchaChallenge

interface CaptchaLogic {

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