package org.testadirapa.sesterzo.cache

import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.testadira.sesterzo.AppDatabase
import org.testadirapa.sesterzo.cache.model.CachedAttachment
import org.testadirapa.sesterzo.model.EncryptedAttachment
import db.CachedAttachment as AttachmentRow

class AndroidAttachmentPersistentCache(
	driver: SqlDriver
) : AttachmentPersistentCache {

	private val db = AppDatabase(driver)
	private val queries = db.attachmentQueries

	private fun upsertInternal(attachment: EncryptedAttachment) {
		queries.upsert(
			id = attachment.id,
			spaceId = attachment.spaceId,
			encryptedSelf = attachment.encryptedSelf,
			inserted_at = System.currentTimeMillis()
		)
	}

	private fun rowToEntity(row: AttachmentRow): CachedAttachment = CachedAttachment(
		id = row.id,
		entity = EncryptedAttachment(
			id = row.id,
			spaceId = row.spaceId,
			encryptedSelf = row.encryptedSelf
		),
		insertedAt = row.inserted_at
	)

	override suspend fun upsert(entity: EncryptedAttachment) = withContext(Dispatchers.IO) {
		upsertInternal(entity)
	}

	override suspend fun upsertAll(entities: List<EncryptedAttachment>) {
		withContext(Dispatchers.IO) {
			db.transaction {
				entities.forEach { upsertInternal(it) }
			}
		}
	}

	override suspend fun getAll(): List<CachedAttachment> = withContext(Dispatchers.IO) {
		queries.selectAll().executeAsList().map { rowToEntity(it) }
	}

	override suspend fun clear(entity: EncryptedAttachment) {
		withContext(Dispatchers.IO) {
			queries.deleteOne(entity.id)
		}
	}

	override suspend fun getById(id: String): CachedAttachment? = withContext(Dispatchers.IO) {
		queries.selectById(id).executeAsOneOrNull()?.let { rowToEntity(it) }
	}

	override suspend fun clearAll() {
		withContext(Dispatchers.IO) {
			queries.deleteAll()
		}
	}
}