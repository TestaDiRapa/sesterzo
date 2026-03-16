package org.testadirapa.sesterzo.config

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.origin
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import kotlin.time.Duration.Companion.seconds

const val LOGIN_RATE_LIMIT_KEY = "login"
const val REGISTER_RATE_LIMIT_KEY = "register"

fun Application.configureThrottling() {
	install(RateLimit) {
		register(RateLimitName(LOGIN_RATE_LIMIT_KEY)) {
			rateLimiter(limit = 30, refillPeriod = 60.seconds)
			requestKey { applicationCall ->
				applicationCall.request.origin.remoteHost
			}
		}

		register(RateLimitName(REGISTER_RATE_LIMIT_KEY)) {
			rateLimiter(limit = 30, refillPeriod = 60.seconds)
			requestKey { applicationCall ->
				applicationCall.request.origin.remoteHost
			}
		}

	}
}
