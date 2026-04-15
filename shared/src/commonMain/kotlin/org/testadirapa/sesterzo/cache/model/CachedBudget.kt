package org.testadirapa.sesterzo.cache.model

import kotlinx.serialization.Serializable
import org.testadirapa.sesterzo.model.EncryptedBudget
import org.testadirapa.sesterzo.model.Timestamp

@Serializable
data class CachedBudget(
	override val id: String,
	override val entity: EncryptedBudget,
	override val insertedAt: Timestamp
) : EntityWithInsertionTs<EncryptedBudget>