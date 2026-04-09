package org.testadirapa.sesterzo.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.User

abstract class UserDAO(client: DBClient) : GenericSingleCollectionDAO<User>(client) {
	override val collection: MongoCollection<User> = client.getCollection()

	/**
	 * Retrieves the [User] where [User.email] is equal to [email].
	 *
	 * @param email the email.
	 * @return the [User], if one exists with the specified [User.email], and null otherwise.
	 */
	abstract suspend fun getByEmail(email: String): User?

	abstract suspend fun setPublicKey(userId: String, publicKey: Base64String): User?

	abstract suspend fun setBackupConfirmation(userId: String): User?
}
