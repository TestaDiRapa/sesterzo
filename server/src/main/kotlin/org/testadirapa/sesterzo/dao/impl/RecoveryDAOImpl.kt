package org.testadirapa.sesterzo.dao.impl

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.dao.RecoveryDAO
import org.testadirapa.sesterzo.model.RecoveryKey

class RecoveryDAOImpl(client: DBClient) : RecoveryDAO(client) {

	override suspend fun getByIdUser(
		recoveryKeyId: String,
		userId: String
	): RecoveryKey? = get(
		filter = Filters.and(
			eq("_id", recoveryKeyId),
			eq(RecoveryKey::owner.name, userId),
		),
	)

}
