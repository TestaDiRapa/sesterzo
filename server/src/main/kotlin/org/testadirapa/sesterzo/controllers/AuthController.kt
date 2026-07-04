package org.testadirapa.sesterzo.controllers

import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.doublereceive.DoubleReceive
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.testadirapa.sesterzo.config.CAPTCHA_RATE_LIMIT_KEY
import org.testadirapa.sesterzo.config.LOGIN_RATE_LIMIT_KEY
import org.testadirapa.sesterzo.config.REFRESH_CTX
import org.testadirapa.sesterzo.config.REGISTER_RATE_LIMIT_KEY
import org.testadirapa.sesterzo.exceptions.JwtException
import org.testadirapa.sesterzo.logic.AuthenticationLogic
import org.testadirapa.sesterzo.logic.CaptchaLogic
import org.testadirapa.sesterzo.model.dto.CompleteRegistrationData
import org.testadirapa.sesterzo.model.dto.LoginData
import org.testadirapa.sesterzo.model.dto.OttData
import org.testadirapa.sesterzo.model.dto.StartRegistrationData
import org.testadirapa.sesterzo.security.toJWTRefreshClaims

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
					authLogic.startRegistration(
						email = data.email,
						name = data.name,
						logsOptIn = data.logsOptIn,
						solution = data.captchaSolution
					)
				)
			}

			post("/completeRegistration") {
				val data = call.receive<CompleteRegistrationData>()
				call.respond(
					authLogic.completeRegistration(processId = data.processId, token = data.token)
				)
			}
		}

		rateLimit(RateLimitName(LOGIN_RATE_LIMIT_KEY)) {
			install(DoubleReceive)

			post("/ott") {
				val ottData = call.receive<OttData>()
				call.respond(
					authLogic.generateOTT(email = ottData.email, solution = ottData.captchaSolution)
				)
			}

			post("/login") {
				val loginData = call.receive<LoginData>()
				call.respond(
					authLogic.login(email = loginData.email, token = loginData.token)
				)
			}
		}

		authenticate(REFRESH_CTX) {
			post("/refresh") {
				val claims = call.principal<JWTPrincipal>()?.payload?.toJWTRefreshClaims()
						?: throw JwtException("No JWT passed in the request")
				val refreshToken = call.request.headers["Authorization"]?.removePrefix("Bearer ")
					?: throw JwtException("No JWT passed in the request")
				call.respond(authLogic.refresh(userId = claims.userId, refreshToken = refreshToken))
			}
		}

	}
