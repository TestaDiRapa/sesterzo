package org.testadirapa.sesterzo.cache.model

import org.testadirapa.sesterzo.model.EncryptedAttachment
import org.testadirapa.sesterzo.model.Timestamp

data class CachedAttachment(
	override val id: String,
	override val entity: EncryptedAttachment,
	override val insertedAt: Timestamp
) : EntityWithInsertionTs<EncryptedAttachment>
