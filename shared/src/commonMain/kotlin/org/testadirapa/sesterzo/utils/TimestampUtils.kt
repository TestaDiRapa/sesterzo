package org.testadirapa.sesterzo.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.testadirapa.sesterzo.model.Timestamp
import kotlin.time.Instant

fun Timestamp.toLocalDateTime(): LocalDateTime =
	Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault())