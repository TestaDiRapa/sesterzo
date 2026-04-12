package org.testadirapa.sesterzo.api

import io.ktor.http.HttpStatusCode
import org.testadirapa.sesterzo.api.impl.AbstractApi
import org.testadirapa.sesterzo.cache.PersistenceOperator
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.model.Identifiable

abstract class CachedApi<W : Identifiable, R : Identifiable>(
	httpConfig: HttpConfig,
	protected val cache: PersistenceOperator<W, R>
) : AbstractApi(httpConfig) {

	protected suspend fun putInCache(data: W) {
		cache.upsert(data)
	}

	protected open fun isInvalid(data: R): Boolean = false

	protected abstract fun convert(data: R): W

	/**
	 * This method retrieves an entity by id. It falls back to a cached element if
	 * - The element is present and valid
	 * - The element is present, not valid, but the fetch from network returned a server error.
	 */
	protected suspend inline fun getWithFallback(
		id: String,
		getFromNetwork: suspend (id: String) -> HttpResponse<W>,
	): W? {
		val cached = cache.getById(id)
		return if (cached == null || isInvalid(cached)) {
			val response = getFromNetwork(id)
			when {
				response.isSuccess -> response.bodyOrThrow().also { putInCache(it) }
				cached != null && response.isServerError -> convert(cached)
				else -> response.bodyOrNullIfStatus(HttpStatusCode.NotFound)
			}
		} else {
			convert(cached)
		}
	}

	protected suspend inline fun cachedOrGet(
		id: String,
		getFromNetwork: suspend (id: String) -> HttpResponse<W>,
	): W {
		val cached = cache.getById(id)
		return if (cached == null || isInvalid(cached)) {
			val response = getFromNetwork(id)
			when {
				response.isSuccess -> response.bodyOrThrow().also { putInCache(it) }
				cached != null && response.isServerError -> convert(cached)
				else -> response.bodyOrThrow()
			}
		} else {
			convert(cached)
		}
	}

	protected suspend inline fun getAllMerging(
		getFromNetwork: suspend () -> HttpResponse<List<W>>,
	): List<W> {
		val cached = cache.getAll().associateBy { it.id }.toMutableMap()
		val response = getFromNetwork()
		return if (response.isSuccess || response.isClientError) {
			response.bodyOrThrow().onEach {
				cache.upsert(it)
				cached.remove(it.id)
			}.also { _ ->
				cached.values.forEach {
					cache.clear(convert(it))
				}
			}
		} else {
			cached.values.map { convert(it) }
		}
	}

}