package org.testadirapa.sesterzo.dao

import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndReplaceOptions
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.ReturnDocument
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.bson.conversions.Bson
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.model.Identifiable
import kotlin.reflect.KProperty

/**
 * Defines all the generic operations that a DAO should implement.
 */
abstract class GenericSingleCollectionDAO<T : Identifiable>(
	protected val client: DBClient,
) {

	/**
	* The [MongoCollection] for this entity type.
	 * Must be instantiated by the concrete class because the type is reified.
	 */
	protected abstract val collection: MongoCollection<T>

	/**
	 * Retrieves a single [T].
	 *
	 * @param filter a [Bson] that identifies the entity.
	 * @return the entity, if one exists with the specified id, and null otherwise.
	 */
	suspend fun get(filter: Bson): T? = collection.find(filter).firstOrNull()

	/**
	 * Retrieves a [T] by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity, if one exists with the specified id, and null otherwise.
	 */
	suspend fun getById(id: String): T? = collection.find(Filters.eq("_id", id)).firstOrNull()

	/**
	 * Retrieves multiple [T]s by id.
	 *
	 * @param ids a [Set] containing the identifiers of the elements to retrieve. Non-existing ids are ignored..
	 * @return a [Flow] of [T] which id is contained in [ids].
	 */
	fun getByIds(ids: Set<String>): Flow<T> = collection.find(Filters.`in`("_id", ids))

	/**
	 * Retrieves all the entities matching the specified filter.
	 *
	 * @param filter a [Bson] filter.
	 * @return a [Flow] of [T].
	 */
	fun find(filter: Bson) = collection.find(filter)

	/**
	 * Retrieves all the [T] in the db.
	 *
	 * @return a [Flow] of [T]
	 */
	fun get(): Flow<T> = collection.find()

	/**
	 * Deletes a [T] by identifier.
	 *
	 * @return true if the operation is successful, false otherwise.
	 */
	suspend fun delete(id: String): Boolean = collection.deleteOne(Filters.eq("_id", id)).deletedCount == 1L

	/**
	 * Creates a new entity [T] in the database.
	 *
	 * @param entity a [T] to create.
	 * @return the id of the entity, if successfully created.
	 */
	suspend fun save(entity: T): T =
		collection.insertOne(entity).insertedId?.asString()?.value?.let { id ->
			getById(id)
		} ?: throw IllegalStateException("There was an error while creating the entity.")

	/**
	 * Replace an existing entity [T] in the database with the version passed as parameter.
	 *
	 * @param entity the new version of the entity [T].
	 * @return the updated entity, if the operation was successful, and false otherwise.
	 */
	suspend fun update(entity: T): T? =
		collection.findOneAndReplace(
			Filters.eq("_id", entity.id),
			entity,
			FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER),
		)

	/**
	 * Creates an ascending index in the current collection for the specified [property], if such an index does not exist already.
	 *
	 * @param property the name of the [KProperty] that will be used in the creation of the index.
	 * @param name the index name.
	 * @param unique whether the index should enforce uniqueness.
	 * @return the name of the newly created index or null if the index already exists.
	 */
	suspend fun initIndex(
		property: String,
		name: String,
		unique: Boolean,
	): String? =
		if (collection.listIndexes().firstOrNull { it["name"] == name } == null) {
			collection.createIndex(
				Indexes.ascending(property),
				IndexOptions().name(name).unique(unique),
			)
		} else {
			null
		}
}
