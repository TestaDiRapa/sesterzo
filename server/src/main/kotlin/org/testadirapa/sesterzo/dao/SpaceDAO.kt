package org.testadirapa.sesterzo.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.model.User

abstract class SpaceDAO(client: DBClient) : GenericDAO<Space>(client) {
	override val collection: MongoCollection<Space> = client.getCollection()

	/**
	 * Retrieves all the [Space]s where [userId] is present as key in [Space.users].
	 *
	 * @param userId the id of the user.
	 * @return a [Flow] of [Space]s matching the condition.
	 */
	abstract fun getByParticipant(userId: String): Flow<Space>
}
