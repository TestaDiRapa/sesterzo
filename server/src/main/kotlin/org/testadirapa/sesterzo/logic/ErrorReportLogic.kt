package org.testadirapa.sesterzo.logic

import org.testadirapa.sesterzo.model.ErrorReport

interface ErrorReportLogic {

	suspend fun saveReport(report: ErrorReport): ErrorReport
}