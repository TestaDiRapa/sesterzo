package org.testadirapa.sesterzo.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun currentDate(): LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date