package org.testadirapa.sesterzo.model.dto

import kotlinx.serialization.Serializable
import org.testadirapa.sesterzo.model.EncryptedEntry

@Serializable
data class BulkEntryUpdate(
	val entriesToCreate: List<EncryptedEntry>,
	val entryIdsToDelete: List<String>,
)