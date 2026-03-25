package org.testadirapa.sesterzo.controllers

import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.testadirapa.sesterzo.config.CAPTCHA_RATE_LIMIT_KEY
import org.testadirapa.sesterzo.config.REGISTER_RATE_LIMIT_KEY
import org.testadirapa.sesterzo.logic.AuthenticationLogic
import org.testadirapa.sesterzo.logic.CaptchaLogic
import org.testadirapa.sesterzo.model.dto.CompleteRegistrationData
import org.testadirapa.sesterzo.model.dto.StartRegistrationData

fun Routing.authController() =
	route("/auth") {
		val authLogic by inject<AuthenticationLogic>()
		val captchaLogic by inject<CaptchaLogic>()

		rateLimit(RateLimitName(CAPTCHA_RATE_LIMIT_KEY)) {

			get("/captcha/{input}") {
				val input = requireNotNull(call.parameters["input"]) { "Input must be specified for captcha" }
				call.respond(captchaLogic.generateChallenge(input))
			}

		}

		rateLimit(RateLimitName(REGISTER_RATE_LIMIT_KEY)) {
			post("/startRegistration") {
				val data = call.receive<StartRegistrationData>()
				call.respond(
					authLogic.startRegistration(email = data.email, name = data.name, solution = data.captchaSolution)
				)
			}

			post("/completeRegistration") {
				val data = call.receive<CompleteRegistrationData>()
				call.respond(
					authLogic.completeRegistration(processId = data.processId, token = data.token)
				)
			}
		}

	}
