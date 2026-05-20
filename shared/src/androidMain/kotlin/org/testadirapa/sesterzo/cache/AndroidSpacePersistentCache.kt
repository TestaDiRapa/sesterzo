package org.testadirapa.sesterzo.cache

import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.testadira.sesterzo.AppDatabase
import org.testadirapa.sesterzo.cache.model.CachedSpace
import db.CachedSpace as SpaceRow
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.serialization.Serialization

class AndroidSpacePersistentCache(
	driver: SqlDriver
) : SpacePersistentCache {

	private val db = AppDatabase(driver)
	private val queries = db.spaceQueries

	private fun upsertInternal(space: Space) {
		queries.upsert(
			id = space.id,
			version = space.version.toLong(),
			name = space.name,
			owner = space.owner,
			fixed_expenses_template_id = space.fixedExpensesTemplateId,
			income_sources_template_id = space.incomeSourcesTemplateId,
			savings_template_id = space.savingsTemplateId,
			users = Serialization.json.encodeToString(space.users),
			pictureReference = space.pictureReference,
			color = space.color?.let { Serialization.json.encodeToString(it) },
			inserted_at = System.currentTimeMillis()
		)
	}

	private fun rowToEntity(row: SpaceRow): CachedSpace =
		CachedSpace(
			id = row.id,
			entity = Space(
				id = row.id,
				version = row.version.toInt(),
				name = row.name,
				owner = row.owner,
				fixedExpensesTemplateId = row.fixed_expenses_template_id,
				incomeSourcesTemplateId = row.income_sources_template_id,
				savingsTemplateId = row.savings_template_id,
				users = Serialization.json.decodeFromString(row.users),
				color = row.color?.let { Serialization.json.decodeFromString(it) },
				pictureReference = row.pictureReference,
			),
			insertedAt = row.inserted_at
		)

	override suspend fun upsert(entity: Space) = withContext(Dispatchers.IO) {
		upsertInternal(entity)
	}

	override suspend fun upsertAll(entities: List<Space>) {
		withContext(Dispatchers.IO) {
			db.transaction {
				entities.forEach { upsertInternal(it) }
			}
		}
	}

	override suspend fun getAll(): List<CachedSpace> = withContext(Dispatchers.IO) {
		queries.selectAll().executeAsList().map { rowToEntity(it) }
	}

	override suspend fun clear(entity: Space) {
		withContext(Dispatchers.IO) {
			queries.deleteOne(entity.id)
		}
	}

	override suspend fun getById(id: String): CachedSpace? = withContext(Dispatchers.IO) {
		queries.selectById(id).executeAsOneOrNull()?.let { rowToEntity(it) }
	}

	override suspend fun getByIds(ids: List<String>): List<CachedSpace> = withContext(Dispatchers.IO) {
		queries.selectByIds(ids).executeAsList().map { rowToEntity(it) }
	}

	override suspend fun clearAll() {
		withContext(Dispatchers.IO) {
			queries.deleteAll()
		}
	}
}