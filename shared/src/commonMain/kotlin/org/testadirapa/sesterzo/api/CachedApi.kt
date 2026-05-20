package org.testadirapa.sesterzo.api

import io.ktor.http.HttpStatusCode
import org.testadirapa.sesterzo.api.impl.AbstractApi
import org.testadirapa.sesterzo.cache.PersistenceOperator
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.model.Identifiable

/**
 * Base class for all cached APIs. [W] is the type of data passed to the cache for writing while [R] is the type of
 * data retrieved from the cache. They may differ for entities where it is important to know when they were inserted in
 * the cache but without that information in the class.
 */
abstract class CachedApi<W : Identifiable, R : Identifiable, C : PersistenceOperator<W, R>>(
	httpConfig: HttpConfig,
	protected val cache: C
) : AbstractApi(httpConfig) {

	protected suspend fun putInCache(data: W) {
		cache.upsert(data)
	}

	/**
	 * Checks if an entity from the cache is invalid. Default is false (always valid), but is overridden in the temporized
	 * implementation.
	 */
	protected open fun isInvalid(data: R): Boolean = false

	/**
	 * Converts the entity type read from the cache to entity type that can be written in the cache (usually the base
	 * entity type, without information of when the element was inserted).
	 */
	protected abstract fun convert(data: R): W

	/**
	 * On a forbidden response from the server, clear the element that was attempted from the cache, or clear the
	 * whole cache of that type if no element was specified.
	 * When the permission to access a space is revoked from a user, this will help clearing the data on device at the
	 * first failed request.
	 */
	protected suspend fun <T> HttpResponse<T>.clearCacheOnForbidden(cachedEntity: R?): T {
		if (cachedEntity != null) {
			cache.clear(convert(cachedEntity))
		} else {
			cache.clearAll()
		}
		return bodyOrThrow()
	}

	/**
	 * Retrieves an element, retrieving it from the server if:
	 * - The [bypassCache] parameter is set to true.
	 * - The element is missing in the cache.
	 * - The element is available in the cache but not up-to-date anymore (according to [isInvalid]).
	 * In this last case, the method will still return the outdated version if the server responds with a 500.
	 * On a successful retrieve, the entity is updated in the cache.
	 * On a 403, [clearCacheOnForbidden] is called on the entity.
	 *
	 * @param id the id of the entity to retrieve.
	 * @param bypassCache forces the retrieval of the entity from the server.
	 * @param getFromNetwork the call to the api function to retrieve the entity from the server.
	 * @return the entity, or null if no entity with that id was found.
	 */
	protected suspend inline fun cachedOrGetIfPresent(
		id: String,
		bypassCache: Boolean,
		getFromNetwork: suspend (id: String) -> HttpResponse<W>,
	): W? = cachedOrGetIfPresent(
		getFromCache = { cache.getById(id) },
		bypassCache = bypassCache,
		getFromNetwork = { getFromNetwork(id) },
	)

	/**
	 * Works exactly like [cachedOrGetIfPresent] but receiving in the [getFromCache] parameter the cache retriever.
	 */
	protected suspend inline fun cachedOrGetIfPresent(
		getFromCache: suspend () -> R?,
		bypassCache: Boolean,
		getFromNetwork: suspend () -> HttpResponse<W>,
	): W? {
		val cached = getFromCache()
		return if (bypassCache || cached == null || isInvalid(cached)) {
			val response = getFromNetwork()
			when {
				response.isSuccess -> response.bodyOrThrow().also { putInCache(it) }
				response.isForbidden -> response.clearCacheOnForbidden(cachedEntity = cached)
				cached != null && response.isServerError -> convert(cached)
				else -> response.bodyOrNullIfStatus(HttpStatusCode.NotFound)
			}
		} else {
			convert(cached)
		}
	}

	/**
	 * Retrieves an element, retrieving it from the server if:
	 * - The [bypassCache] parameter is set to true.
	 * - The element is missing in the cache.
	 * - The element is available in the cache but not up-to-date anymore (according to [isInvalid]).
	 * In this last case, the method will still return the outdated version if the server responds with a 500.
	 * On a successful retrieve, the entity is updated in the cache.
	 * On a 403, [clearCacheOnForbidden] is called on the entity.
	 *
	 * @param id the id of the entity to retrieve.
	 * @param bypassCache forces the retrieval of the entity from the server.
	 * @param getFromNetwork the call to the api function to retrieve the entity from the server.
	 * @return the entity.
	 */
	protected suspend inline fun cachedOrGet(
		id: String,
		bypassCache: Boolean,
		getFromNetwork: suspend (id: String) -> HttpResponse<W>,
	): W {
		val cached = cache.getById(id)
		return if (bypassCache || cached == null || isInvalid(cached)) {
			val response = getFromNetwork(id)
			when {
				response.isSuccess -> response.bodyOrThrow().also { putInCache(it) }
				response.isForbidden -> response.clearCacheOnForbidden(cachedEntity = cached)
				cached != null && response.isServerError -> convert(cached)
				else -> response.bodyOrThrow()
			}
		} else {
			convert(cached)
		}
	}

	/**
	 * Retrieves multiple elements by their ids, retrieving from the server the ones that are not present in cache or
	 * that are not up-to-date anymore according to [isInvalid].
	 * This method will attempt to retrieve all entities from the server if [bypassCache] is true.
	 * In this last case, the method will still return the outdated versions if the server responds with a 500.
	 * On a successful retrieve, the entities are updated in the cache.
	 * On a 403, [clearCacheOnForbidden] is called on all the entities of the type.
	 *
	 * @param ids the ids of the entity to retrieve.
	 * @param bypassCache forces the retrieval of the entities from the server.
	 * @param getFromNetwork the call to the api function to retrieve the entity from the server.
	 * @return the list of entities.
	 */
	protected suspend fun cachedAndGetMissing(
		ids: List<String>,
		bypassCache: Boolean,
		getFromNetwork: suspend (ids: List<String>) -> HttpResponse<List<W>>,
	): List<W> {
		val cachedByIds = cache.getByIds(ids).associateBy { it.id }.toMutableMap()
		val idsToRetrieve = if (bypassCache) {
			ids
		} else {
			ids.filter { !cachedByIds.containsKey(it) || isInvalid(cachedByIds.getValue(it)) }
		}
		return if (idsToRetrieve.isEmpty()) {
			cachedByIds.values.map { convert(it) }
		} else {
			val response = getFromNetwork(idsToRetrieve)
			when {
				response.isSuccess -> response.bodyOrThrow().onEach {
					cachedByIds.remove(it.id)
					putInCache(it)
				} + cachedByIds.values.map { convert(it) }
				response.isForbidden -> response.clearCacheOnForbidden(cachedEntity = null)
				response.isServerError -> cachedByIds.values.map { convert(it) }
				else -> response.bodyOrThrow()
			}
		}
	}

	/**
	 * Retrieves all the entities for the type.
	 * It will always attempt to retrieve the entities from the network.
	 * - If the request fails with a 500, the cached elements are returned (if any).
	 * - If the request fails with a 403, the cache is cleared and the method throws an exception.
	 * - If the request succeeds, the element retrieved with the request are returned. The element that are present
	 * in the cache but not in the result from the request are cleared from the cache.
	 *
	 * @param getFromNetwork the request to retrieve all the entities of a type from the network.
	 * @return a [List] of [W].
	 */
	protected suspend inline fun getAllMerging(
		getFromNetwork: suspend () -> HttpResponse<List<W>>,
	): List<W> = parseResponse(
		cached = cache.getAll().associateBy { it.id }.toMutableMap(),
		response = getFromNetwork()
	)

	/**
	 * Retrieves a subset of the entities from the cache, falling back to the server based on the result of [bypassCacheCondition].
	 * The handling of the request follows the same logic as [getAllMerging].
	 *
	 * @param getFromCache retrieves the entities from the cache.
	 * @param getFromNetwork the request to retrieve all the entities of a type from the network.
	 * @param bypassCacheCondition a function that takes as input the cached elements and returns true if the method has
	 * to bypass the cached entities.
	 * @return a [List] of [W].
	 */
	protected suspend inline fun getAllMergingIf(
		getFromCache: suspend () -> List<R>,
		getFromNetwork: suspend () -> HttpResponse<List<W>>,
		bypassCacheCondition: suspend (List<W>) -> Boolean,
	): List<W> {
		val cached = getFromCache()
			.associateByTo(LinkedHashMap()) { it.id }
			.toMutableMap()
		val cachedConverted = cached.values.map { convert(it) }
		return if (!bypassCacheCondition(cachedConverted)) {
				cachedConverted
		} else {
			parseResponse(cached = cached, response = getFromNetwork())
		}
	}

	protected suspend inline fun parseResponse(
		cached: MutableMap<String, R>,
		response: HttpResponse<List<W>>
	): List<W> = when {
		response.isForbidden -> response.clearCacheOnForbidden(cachedEntity = null)
		response.isSuccess || response.isClientError -> {
			response.bodyOrThrow().onEach {
				cache.upsert(it)
				cached.remove(it.id)
			}.also { _ ->
				cached.values.forEach {
					cache.clear(convert(it))
				}
			}
		}
		else -> cached.values.map { convert(it) }
	}

}