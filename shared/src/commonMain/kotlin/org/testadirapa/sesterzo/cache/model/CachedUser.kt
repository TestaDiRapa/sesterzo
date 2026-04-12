package org.testadirapa.sesterzo.cache.model

import org.testadirapa.sesterzo.model.Timestamp
import org.testadirapa.sesterzo.model.User

data class CachedUser(
	override val id: String,
	override val entity: User,
	override val insertedAt: Timestamp
) : EntityWithInsertionTs<User>