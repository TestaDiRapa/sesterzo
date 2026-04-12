package org.testadirapa.sesterzo.api

import org.testadirapa.sesterzo.cache.PersistenceOperator
import org.testadirapa.sesterzo.cache.model.EntityWithInsertionTs
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.model.Identifiable
import kotlin.time.Clock
import kotlin.time.Duration

abstract class TemporizedCachedApi<T : Identifiable, C : EntityWithInsertionTs<T>>(
	httpConfig: HttpConfig,
	cache: PersistenceOperator<T, C>,
	private val ttl: Duration
) : CachedApi<T, C>(httpConfig, cache) {

	override fun isInvalid(data: C): Boolean =
		(Clock.System.now().toEpochMilliseconds() - data.insertedAt) >= ttl.inWholeMilliseconds

	override fun convert(data: C): T = data.entity

}