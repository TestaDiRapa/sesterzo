package org.testadirapa.sesterzo.logic.impl

import org.testadirapa.sesterzo.dao.ErrorReportDAO
import org.testadirapa.sesterzo.logic.ErrorReportLogic
import org.testadirapa.sesterzo.model.ErrorReport

class ErrorReportLogicImpl(
	private val errorReportDAO: ErrorReportDAO,
) : ErrorReportLogic {

	override suspend fun saveReport(report: ErrorReport): ErrorReport =
		errorReportDAO.save(report)

}