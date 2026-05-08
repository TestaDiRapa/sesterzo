package org.testadirapa.sesterzo.dao.impl

import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.dao.AttachmentDAO

class AttachmentDAOImpl(
	client: DBClient,
) : AttachmentDAO(client) {

	override suspend fun initIndexes(spaceId: String) {}

}