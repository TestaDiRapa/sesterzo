package org.testadirapa.sesterzo.model

import com.icure.kryptom.crypto.defaultCryptoService
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.testadirapa.sesterzo.exceptions.ResponseStatusException
import kotlin.time.Clock

@Serializable
data class ErrorReport(
	@SerialName("_id") override val id: String,
	val createdAt: Timestamp,
	val errorMessage: String?,
	val stackTrace: String,
	val requestInfo: ResponseStatusException?
) : Identifiable

fun Throwable.toErrorReport(): ErrorReport = ErrorReport(
	id = defaultCryptoService.strongRandom.randomUUID(),
	createdAt = Clock.System.now().toEpochMilliseconds(),
	errorMessage = message,
	stackTrace = stackTraceToString(),
	requestInfo = this as? ResponseStatusException
)