package org.testadirapa.sesterzo.utils

import androidx.compose.runtime.Composable
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.model.Timestamp
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.april_abbreviated
import sesterzo.composeapp.generated.resources.august_abbreviated
import sesterzo.composeapp.generated.resources.december_abbreviated
import sesterzo.composeapp.generated.resources.february_abbreviated
import sesterzo.composeapp.generated.resources.january_abbreviated
import sesterzo.composeapp.generated.resources.july_abbreviated
import sesterzo.composeapp.generated.resources.june_abbreviated
import sesterzo.composeapp.generated.resources.march_abbreviated
import sesterzo.composeapp.generated.resources.may_abbreviated
import sesterzo.composeapp.generated.resources.november_abbreviated
import sesterzo.composeapp.generated.resources.october_abbreviated
import sesterzo.composeapp.generated.resources.september_abbreviated
import sesterzo.composeapp.generated.resources.april
import sesterzo.composeapp.generated.resources.august
import sesterzo.composeapp.generated.resources.december
import sesterzo.composeapp.generated.resources.february
import sesterzo.composeapp.generated.resources.friday
import sesterzo.composeapp.generated.resources.friday_abbreviated
import sesterzo.composeapp.generated.resources.january
import sesterzo.composeapp.generated.resources.july
import sesterzo.composeapp.generated.resources.june
import sesterzo.composeapp.generated.resources.march
import sesterzo.composeapp.generated.resources.may
import sesterzo.composeapp.generated.resources.monday
import sesterzo.composeapp.generated.resources.monday_abbreviated
import sesterzo.composeapp.generated.resources.november
import sesterzo.composeapp.generated.resources.october
import sesterzo.composeapp.generated.resources.saturday
import sesterzo.composeapp.generated.resources.saturday_abbreviated
import sesterzo.composeapp.generated.resources.september
import sesterzo.composeapp.generated.resources.sunday
import sesterzo.composeapp.generated.resources.sunday_abbreviated
import sesterzo.composeapp.generated.resources.thursday
import sesterzo.composeapp.generated.resources.thursday_abbreviated
import sesterzo.composeapp.generated.resources.tuesday
import sesterzo.composeapp.generated.resources.tuesday_abbreviated
import sesterzo.composeapp.generated.resources.wednesday
import sesterzo.composeapp.generated.resources.wednesday_abbreviated
import kotlin.time.Instant

@Composable
fun monthName(month: Month, abbreviated: Boolean): String = when (month) {
	Month.JANUARY -> if(abbreviated) stringResource(Res.string.january_abbreviated) else stringResource(Res.string.january)
	Month.FEBRUARY -> if(abbreviated) stringResource(Res.string.february_abbreviated) else stringResource(Res.string.february)
	Month.MARCH -> if(abbreviated) stringResource(Res.string.march_abbreviated) else stringResource(Res.string.march)
	Month.APRIL -> if(abbreviated) stringResource(Res.string.april_abbreviated) else stringResource(Res.string.april)
	Month.MAY -> if(abbreviated) stringResource(Res.string.may_abbreviated) else stringResource(Res.string.may)
	Month.JUNE -> if(abbreviated) stringResource(Res.string.june_abbreviated) else stringResource(Res.string.june)
	Month.JULY -> if(abbreviated) stringResource(Res.string.july_abbreviated) else stringResource(Res.string.july)
	Month.AUGUST -> if(abbreviated) stringResource(Res.string.august_abbreviated) else stringResource(Res.string.august)
	Month.SEPTEMBER -> if(abbreviated) stringResource(Res.string.september_abbreviated) else stringResource(Res.string.september)
	Month.OCTOBER -> if(abbreviated) stringResource(Res.string.october_abbreviated) else stringResource(Res.string.october)
	Month.NOVEMBER -> if(abbreviated) stringResource(Res.string.november_abbreviated) else stringResource(Res.string.november)
	Month.DECEMBER -> if(abbreviated) stringResource(Res.string.december_abbreviated) else stringResource(Res.string.december)
}

@Composable
fun dayName(day: DayOfWeek, abbreviated: Boolean): String = when (day) {
	DayOfWeek.MONDAY -> if(abbreviated) stringResource(Res.string.monday_abbreviated) else stringResource(Res.string.monday)
	DayOfWeek.TUESDAY -> if(abbreviated) stringResource(Res.string.tuesday_abbreviated) else stringResource(Res.string.tuesday)
	DayOfWeek.WEDNESDAY -> if(abbreviated) stringResource(Res.string.wednesday_abbreviated) else stringResource(Res.string.wednesday)
	DayOfWeek.THURSDAY -> if(abbreviated) stringResource(Res.string.thursday_abbreviated) else stringResource(Res.string.thursday)
	DayOfWeek.FRIDAY -> if(abbreviated) stringResource(Res.string.friday_abbreviated) else stringResource(Res.string.friday)
	DayOfWeek.SATURDAY -> if(abbreviated) stringResource(Res.string.saturday_abbreviated) else stringResource(Res.string.saturday)
	DayOfWeek.SUNDAY -> if(abbreviated) stringResource(Res.string.sunday_abbreviated) else stringResource(Res.string.sunday)
}

fun Timestamp.toLocalDate() =
	Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault()).date