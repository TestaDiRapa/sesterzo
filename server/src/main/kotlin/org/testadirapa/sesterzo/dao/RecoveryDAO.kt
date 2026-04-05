package org.testadirapa.sesterzo.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.RecoveryKey
import org.testadirapa.sesterzo.model.User

abstract class RecoveryDAO(client: DBClient) : GenericDAO<RecoveryKey>(client) {
	override val collection: MongoCollection<RecoveryKey> = client.getCollection()

	/**
	 * Retrieves a recovery key by [RecoveryKey.id] and [RecoveryKey.owner].
	 *
	 * @param recoveryKeyId the id of the recovery key.
	 * @param userId the id of the user owner of the key.
	 * @return a [RecoveryKey], or null if not found.
	 */
	abstract suspend fun getByIdUser(recoveryKeyId: String, userId: String): RecoveryKey?
}
