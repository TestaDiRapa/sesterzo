package org.testadirapa.sesterzo.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.testadirapa.sesterzo.model.Budget
import kotlin.time.Clock

typealias BudgetReference = LocalDate

fun currentBudgetReference(): BudgetReference = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.let {
	LocalDate(year = it.year, month = it.month.number, day = 1)
}

fun Budget.toReference() = LocalDate(year = year, month = month, day = 1)
fun Budget.nextReference() = toReference().nextReference()
fun Budget.previousReference() = toReference().previousReference()

fun BudgetReference.toBudgetId() = Budget.getBudgetId(year = year, month = month.number)

fun BudgetReference.previousReference() = minus(1, DateTimeUnit.MonthBased(1))
fun BudgetReference.nextReference() = plus(1, DateTimeUnit.MonthBased(1))