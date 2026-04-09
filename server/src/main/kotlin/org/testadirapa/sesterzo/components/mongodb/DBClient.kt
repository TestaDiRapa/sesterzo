package org.testadirapa.sesterzo.components.mongodb

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.testadirapa.sesterzo.model.Identifiable

/**
 * Instantiates a client for the database that will be used by all the other classes
 */
class DBClient(
	dbCredentials: MongoDBCredentials,
) {
	private val client = MongoClient.create(dbCredentials.toConnectionString())
	val db = client.getDatabase(dbCredentials.databaseName)

	/**
	 * Get a collection of a type of [Identifiable]. The collection name is the simple name of the concrete entity.
	 *
	 * @param E the type of entity that the collection contains.
	 * @return a [MongoCollection] of [E].
	 */
	inline fun <reified E : Identifiable> getCollection(): MongoCollection<E> =
		db.getCollection<E>(
			E::class.simpleName ?: throw IllegalArgumentException("Cannot find collection for ${E::class}"),
		)

	/**
	 * Get a collection of a type of [Identifiable] in a space.
	 * The collection name is the simple name of the concrete entity concatenated to the [spaceId].
	 *
	 * @param E the type of entity that the collection contains.
	 * @param spaceId the space id.
	 * @return a [MongoCollection] of [E].
	 */
	inline fun <reified E : Identifiable> getCollection(spaceId: String): MongoCollection<E> =
		db.getCollection<E>(
			buildString {
				append(E::class.simpleName ?: throw IllegalArgumentException("Cannot find collection for ${E::class}"))
				append("-")
				append(spaceId)
			}
		)
}