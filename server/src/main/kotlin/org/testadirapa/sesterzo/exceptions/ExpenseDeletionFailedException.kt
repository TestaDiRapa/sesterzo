package org.testadirapa.sesterzo.exceptions

import io.ktor.http.HttpStatusCode

class ExpenseDeletionFailedException(expenseId: String) : HttpException("Expense $expenseId deletion failed")  {
	override val statusCode: HttpStatusCode = HttpStatusCode.BadRequest
	override val label: ExceptionLabel = ExceptionLabel.ExpenseDeletionFailed
}
