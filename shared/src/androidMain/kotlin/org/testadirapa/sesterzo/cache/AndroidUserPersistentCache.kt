package org.testadirapa.sesterzo.cache

import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.testadira.sesterzo.AppDatabase
import org.testadirapa.sesterzo.cache.model.CachedUser
import org.testadirapa.sesterzo.model.Currency
import db.CachedUser as UserRow
import org.testadirapa.sesterzo.model.User
import org.testadirapa.sesterzo.serialization.Serialization

class AndroidUserPersistentCache(
	driver: SqlDriver
) : UserPersistentCache {

	private val db = AppDatabase(driver)
	private val queries = db.userQueries

	private fun upsertInternal(user: User) {
		queries.upsert(
			id = user.id,
			name = user.name,
			email = user.email,
			authenticationTokens = Serialization.json.encodeToString(user.authenticationTokens),
			publicKey = user.publicKey,
			hasBackup = if (user.hasBackup) 1 else 0,
			preferredCurrency = user.preferredCurrency.name,
			sendLogs = if (user.sendLogs) 1 else 0,
			inserted_at = System.currentTimeMillis()
		)
	}

	private fun rowToEntity(row: UserRow): CachedUser = CachedUser(
		id = row.id,
		entity = User(
			id = row.id,
			name = row.name,
			email = row.email,
			authenticationTokens = Serialization.json.decodeFromString(row.authenticationTokens),
			preferredCurrency = Currency.valueOf(row.preferredCurrency),
			publicKey = row.publicKey,
			hasBackup = row.hasBackup == 1L,
			sendLogs = row.sendLogs == 1L,
		),
		insertedAt = row.inserted_at
	)

	override suspend fun upsert(entity: User) = withContext(Dispatchers.IO) {
		upsertInternal(entity)
	}

	override suspend fun upsertAll(entities: List<User>) {
		withContext(Dispatchers.IO) {
			db.transaction {
				entities.forEach { upsertInternal(it) }
			}
		}
	}

	override suspend fun getAll(): List<CachedUser> = withContext(Dispatchers.IO) {
		queries.selectAll().executeAsList().map { rowToEntity(it) }
	}

	override suspend fun clear(entity: User) {
		withContext(Dispatchers.IO) {
			queries.deleteOne(entity.id)
		}
	}

	override suspend fun getById(id: String): CachedUser? = withContext(Dispatchers.IO) {
		queries.selectById(id).executeAsOneOrNull()?.let { rowToEntity(it) }
	}

	override suspend fun getByIds(ids: List<String>): List<CachedUser> = withContext(Dispatchers.IO) {
		queries.selectByIds(ids).executeAsList().map { rowToEntity(it) }
	}

	override suspend fun clearAll() {
		withContext(Dispatchers.IO) {
			queries.deleteAll()
		}
	}
}