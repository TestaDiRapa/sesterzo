package org.testadirapa.sesterzo.http

import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import kotlinx.serialization.json.Json
import org.testadirapa.sesterzo.exceptions.ResponseStatusException
import org.testadirapa.sesterzo.model.dto.StatusResponse
import io.ktor.client.statement.HttpResponse as KtorHttpResponse

class HttpResponse<T>(
	private val response: KtorHttpResponse,
	val typeInfo: TypeInfo,
	val patcher: (T) -> (T) = { it }
) {

	val isSuccess: Boolean get() = response.status.isSuccess()
	val isServerError: Boolean get() = response.status.value >= 500
	val isClientError: Boolean get() = response.status.value in 400 until 500
	val isForbidden: Boolean get() = response.status.value == 403
	val isConflict: Boolean get() = response.status.value == 409

	@Suppress("UNCHECKED_CAST")
	private suspend fun getBodyFromResponse(): T =
		patcher((response.call.bodyNullable(typeInfo) as T))

	private suspend fun throwErrorFromResponse(): Nothing {
		throw exception()
	}

	suspend fun exception(): Throwable {
		val responseBodyText = kotlin.runCatching {
			response.bodyAsText()
		}.getOrNull()
		val statusResponse = responseBodyText?.let { Json.decodeFromString<StatusResponse>(it) }
		return ResponseStatusException(
			msg = statusResponse?.message,
			status = response.status.value,
			label = statusResponse?.label,
			url = response.request.url.toString(),
			method = response.request.method.value,
		)
	}

	suspend fun bodyOrThrow(): T =
		if (isSuccess) {
			getBodyFromResponse()
		} else {
			throwErrorFromResponse()
		}

	suspend fun bodyOrNull(): T? =
		if (isSuccess) {
			getBodyFromResponse()
		} else {
			null
		}

	suspend fun bodyOrNullIfStatus(status: HttpStatusCode): T? = when {
		isSuccess -> getBodyFromResponse()
		response.status == status -> null
		else -> throwErrorFromResponse()
	}

}

inline fun <reified T> KtorHttpResponse.wrap(): HttpResponse<T> = HttpResponse(this, typeInfo<T>())

inline fun <reified T> KtorHttpResponse.wrapPatching(noinline patcher: (T) -> (T)): HttpResponse<T> =
	HttpResponse(this, typeInfo<T>(), patcher)