package org.testadirapa.sesterzo.config

import io.ktor.client.HttpClient
import kotlin.time.Duration

data class HttpConfig(
	val baseUrl: String,
	val client: HttpClient,
	val maxRetriesOnServerError: Int,
	val delayOnRetry: Duration
)