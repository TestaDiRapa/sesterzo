package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

class BudgetConflictException(budgetId: String) : HttpException("New version of budget $budgetId exists")  {
	override val label: ExceptionLabel = ExceptionLabel.BudgetConflict
	override val statusCode: HttpStatusCode = HttpStatusCode.Conflict
}
