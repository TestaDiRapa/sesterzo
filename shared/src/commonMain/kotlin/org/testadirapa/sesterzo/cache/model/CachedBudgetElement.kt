package org.testadirapa.sesterzo.cache.model

import org.testadirapa.sesterzo.model.EncryptedBudgetElement
import org.testadirapa.sesterzo.model.Timestamp

data class CachedBudgetElement(
	override val id: String,
	override val entity: EncryptedBudgetElement,
	override val insertedAt: Timestamp
): EntityWithInsertionTs<EncryptedBudgetElement>