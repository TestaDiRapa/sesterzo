package org.testadirapa.sesterzo.dao.impl

import org.testadirapa.sesterzo.components.mongodb.DBClient
import org.testadirapa.sesterzo.dao.ErrorReportDAO

class ErrorReportDAOImpl(client: DBClient) : ErrorReportDAO(client)
