package org.testadirapa.sesterzo.dao.impl

import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.dao.SpaceDAO
import org.testadirapa.sesterzo.model.Space

class SpaceDAOImpl(client: DBClient) : SpaceDAO(client) {

	override fun getByParticipant(userId: String): Flow<Space> =
		collection.find(Filters.exists("${Space::users.name}.$userId"))

}