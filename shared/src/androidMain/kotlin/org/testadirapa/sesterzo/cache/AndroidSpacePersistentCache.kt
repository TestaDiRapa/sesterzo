package org.testadirapa.sesterzo.cache

import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.testadira.sesterzo.AppDatabase
import org.testadirapa.sesterzo.cache.model.SpaceEntity
import db.SpaceEntity as SpaceRow
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
			picture = space.picture,
			inserted_at = System.currentTimeMillis()
		)
	}

	private fun rowToEntity(row: SpaceRow): SpaceEntity {
		return SpaceEntity(
			id = row.id,
			space = Space(
				id = row.id,
				version = row.version.toInt(),
				name = row.name,
				owner = row.owner,
				fixedExpensesTemplateId = row.fixed_expenses_template_id,
				incomeSourcesTemplateId = row.income_sources_template_id,
				savingsTemplateId = row.savings_template_id,
				users = Serialization.json.decodeFromString(row.users),
				picture = row.picture
			),
			insertedAt = row.inserted_at
		)
	}

	override suspend fun upsert(space: Space) = withContext(Dispatchers.IO) {
		upsertInternal(space)
	}

	override suspend fun upsertAll(spaces: List<Space>) {
		withContext(Dispatchers.IO) {
			db.transaction {
				spaces.forEach { upsertInternal(it) }
			}
		}
	}

	override suspend fun getAll(): List<SpaceEntity> = withContext(Dispatchers.IO) {
		queries.selectAll().executeAsList().map { rowToEntity(it) }
	}

	override suspend fun getById(id: String): SpaceEntity? = withContext(Dispatchers.IO) {
		queries.selectById(id).executeAsOneOrNull()?.let { rowToEntity(it) }
	}

	override suspend fun clearAll() {
		withContext(Dispatchers.IO) {
			queries.deleteAll()
		}
	}
}