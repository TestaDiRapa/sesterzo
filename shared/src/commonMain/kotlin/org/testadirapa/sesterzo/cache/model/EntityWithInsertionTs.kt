package org.testadirapa.sesterzo.cache.model

import org.testadirapa.sesterzo.model.Identifiable
import org.testadirapa.sesterzo.model.Timestamp

interface EntityWithInsertionTs<T> : Identifiable {
	val entity: T
	val insertedAt: Timestamp
}