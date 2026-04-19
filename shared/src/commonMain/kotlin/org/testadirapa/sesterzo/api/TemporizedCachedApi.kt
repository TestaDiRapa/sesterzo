package org.testadirapa.sesterzo.api

import org.testadirapa.sesterzo.cache.PersistenceOperator
import org.testadirapa.sesterzo.cache.model.EntityWithInsertionTs
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.model.Identifiable
import kotlin.time.Clock
import kotlin.time.Duration

abstract class TemporizedCachedApi<T : Identifiable, C : EntityWithInsertionTs<T>, P: PersistenceOperator<T, C>>(
	httpConfig: HttpConfig,
	cache: P,
	private val ttl: Duration
) : CachedApi<T, C, P>(httpConfig, cache) {

	/**
	 * Overrides the default behaviour by taking into account the timestamp of insertion of the entity.
	 */
	override fun isInvalid(data: C): Boolean =
		(Clock.System.now().toEpochMilliseconds() - data.insertedAt) >= ttl.inWholeMilliseconds

	override fun convert(data: C): T = data.entity

}