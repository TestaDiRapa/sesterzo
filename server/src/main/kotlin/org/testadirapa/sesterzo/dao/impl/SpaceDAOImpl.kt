package org.testadirapa.sesterzo.dao.impl

import com.mongodb.client.model.Filters
import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.dao.SpaceDAO
import org.testadirapa.sesterzo.model.Space

class SpaceDAOImpl(client: DBClient) : SpaceDAO(client) {

	override fun getByParticipant(userId: String): Flow<Space> =
		find(Filters.exists("${Space::users.name}.$userId"))

	override fun getByOwner(userId: String): Flow<Space> =
		find(Filters.eq(Space::owner.name, userId))

}