package org.testadirapa.sesterzo.api.impl

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import kotlinx.coroutines.delay
import org.testadirapa.sesterzo.config.HttpConfig

abstract class AbstractApi(
	private val config: HttpConfig,
) {

	protected val baseUrl: String get() = config.baseUrl

	protected suspend fun get(
		block: suspend HttpRequestBuilder.() -> Unit
	): HttpResponse = requestAndRetryOnServerOrConnectionError(
		method = HttpMethod.Get,
		block = block
	)

	protected suspend fun post(
		block: suspend HttpRequestBuilder.() -> Unit
	): HttpResponse = requestAndRetryOnServerOrConnectionError(
		method = HttpMethod.Post,
		block = block
	)

	protected suspend fun put(
		block: suspend HttpRequestBuilder.() -> Unit
	): HttpResponse = requestAndRetryOnServerOrConnectionError(
		method = HttpMethod.Put,
		block = block
	)

	protected suspend fun delete(
		block: suspend HttpRequestBuilder.() -> Unit
	): HttpResponse = requestAndRetryOnServerOrConnectionError(
		method = HttpMethod.Delete,
		block = block
	)

	private tailrec suspend fun requestAndRetryOnServerOrConnectionError(
		method: HttpMethod,
		block: suspend HttpRequestBuilder.() -> Unit,
		remainingRetries: Int = config.maxRetriesOnServerError
	): HttpResponse {
		val result = request(method) { block() }
		return when {
			remainingRetries <= 0 -> result.getOrThrow()
			result.isFailure || result.getOrThrow().status.value in 500 .. 599 -> {
				delay(config.delayOnRetry)
				requestAndRetryOnServerOrConnectionError(
					method,
					block,
					remainingRetries - 1
				)
			}
			else -> result.getOrThrow()
		}
	}

	private suspend inline fun request(
		method: HttpMethod,
		block: HttpRequestBuilder.() -> Unit
	): Result<HttpResponse> {
		val requestBuilder = HttpRequestBuilder().apply {
			this.method = method
			block()
		}
		return kotlin.runCatching { config.client.request(requestBuilder) }
	}

}