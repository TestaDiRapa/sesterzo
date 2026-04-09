package org.testadirapa.sesterzo.dao

import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.bson.conversions.Bson
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.model.Identifiable

/**
 * Defines all the generic operations that a DAO should implement.
 */
abstract class GenericMultiCollectionDAO<T : Identifiable>(
	protected val client: DBClient,
) {
	/**
	 * The [MongoCollection] for this entity type for a specific space.
	 */
	protected abstract fun getCollection(spaceId: String): MongoCollection<T>

	/**
	 * Retrieves a single [T].
	 *
	 * @param spaceId the id of the space.
	 * @param filter a [Bson] that identifies the entity.
	 * @return the entity, if one exists with the specified id, and null otherwise.
	 */
	suspend fun get(spaceId: String, filter: Bson): T? = getCollection(spaceId).find(filter).firstOrNull()

	/**
	 * Retrieves a [T] by id.
	 *
	 * @param spaceId the id of the space.
	 * @param id the id of the entity.
	 * @return the entity, if one exists with the specified id, and null otherwise.
	 */
	suspend fun getById(spaceId: String, id: String): T? = getCollection(spaceId).find(Filters.eq("_id", id)).firstOrNull()

	/**
	 * Retrieves multiple [T]s by id.
	 *
	 * @param spaceId the id of the space.
	 * @param ids a [Set] containing the identifiers of the elements to retrieve. Non-existing ids are ignored..
	 * @return a [Flow] of [T] which id is contained in [ids].
	 */
	fun getByIds(spaceId: String, ids: Set<String>): Flow<T> = getCollection(spaceId).find(Filters.`in`("_id", ids))

	/**
	 * Retrieves all the entities matching the specified filter.
	 *
	 * @param spaceId the id of the space.
	 * @param filter a [Bson] filter.
	 * @return a [Flow] of [T].
	 */
	fun find(spaceId: String, filter: Bson) = getCollection(spaceId).find(filter)

	/**
	 * Retrieves all the [T] in the db.
	 *
	 * @param spaceId the id of the space.
	 * @return a [Flow] of [T]
	 */
	fun get(spaceId: String): Flow<T> = getCollection(spaceId).find()

	/**
	 * Deletes a [T] by identifier.
	 *
	 * @param spaceId the id of the space.
	 * @return true if the operation is successful, false otherwise.
	 */
	suspend fun delete(spaceId: String, id: String): Boolean = getCollection(spaceId).deleteOne(Filters.eq("_id", id)).deletedCount == 1L

	/**
	 * Creates a new entity [T] in the database.
	 *
	 * @param spaceId the id of the space.
	 * @param entity a [T] to create.
	 * @return the id of the entity, if successfully created.
	 */
	suspend fun save(spaceId: String, entity: T): String =
		getCollection(spaceId).insertOne(entity).insertedId?.asString()?.value
			?: throw IllegalStateException("There was an error while creating the entity.")

	/**
	 * Replace an existing entity [T] in the database with the version passed as parameter.
	 *
	 * @param spaceId the id of the space.
	 * @param entity the new version of the entity [T].
	 * @return the updated entity, if the operation was successful, and false otherwise.
	 */
	suspend fun update(spaceId: String, entity: T): T? =
		getCollection(spaceId).findOneAndReplace(
			Filters.eq("_id", entity.id),
			entity,
			FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER),
		)

	abstract suspend fun initIndexes()
}
