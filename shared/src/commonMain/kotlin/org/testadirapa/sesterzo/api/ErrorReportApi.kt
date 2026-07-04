package org.testadirapa.sesterzo.api

import org.testadirapa.sesterzo.model.ErrorReport

interface ErrorReportApi {

	suspend fun sendErrorReport(errorReport: ErrorReport)

}