package org.testadirapa.sesterzo.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.testadirapa.sesterzo.model.Budget
import org.testadirapa.sesterzo.model.Timestamp
import kotlin.time.Clock

typealias BudgetReference = LocalDate

fun currentLocalDate(): LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

fun currentBudgetReference(): BudgetReference = currentLocalDate().let {
	LocalDate(year = it.year, month = it.month.number, day = 1)
}

fun budgetReferenceOf(year: Int, month: Int): BudgetReference = LocalDate(year = year, month = month, day = 1)

fun Budget.toReference() = LocalDate(year = year, month = month, day = 1)
fun Budget.nextReference() = toReference().nextReference()
fun Budget.previousReference() = toReference().previousReference()

fun BudgetReference.toBudgetId() = Budget.getBudgetId(year = year, month = month.number)

fun BudgetReference.previousReference() = minus(1, DateTimeUnit.MonthBased(1))
fun BudgetReference.nextReference() = plus(1, DateTimeUnit.MonthBased(1))

fun BudgetReference.daysToEndOfValidity(): Int =
	if (currentBudgetReference() <= this) {
		val currentDate = currentLocalDate()
		val endOfValidity = nextReference().minus(1, DateTimeUnit.DAY)
		(endOfValidity.toEpochDays() - currentDate.toEpochDays()).toInt()
	} else {
		0
	}

fun BudgetReference.toTimestampOfStartValidity(): Timestamp =
	atTime(0, 1).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()