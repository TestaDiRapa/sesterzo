package org.testadirapa.sesterzo.dao

import com.mongodb.kotlin.client.coroutine.MongoCollection
import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.model.ErrorReport

abstract class ErrorReportDAO(client: DBClient) : GenericSingleCollectionDAO<ErrorReport>(client) {
	override val collection: MongoCollection<ErrorReport> = client.getCollection()
}
