package org.testadirapa.sesterzo.viewmodel.errors

import org.jetbrains.compose.resources.getString
import org.testadirapa.sesterzo.exceptions.ExceptionLabel
import org.testadirapa.sesterzo.exceptions.ResponseStatusException
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.error_generic
import sesterzo.composeapp.generated.resources.error_invalid_captcha
import sesterzo.composeapp.generated.resources.error_invalid_jwt
import sesterzo.composeapp.generated.resources.error_invalid_registration
import sesterzo.composeapp.generated.resources.error_invalid_registration_data
import sesterzo.composeapp.generated.resources.error_unauthorized

data class ErrorState(val message: String)

suspend fun Throwable.toErrorState(): ErrorState = when(this) {
	is ResponseStatusException -> {
		when (label) {
			ExceptionLabel.InvalidCaptcha -> getString(Res.string.error_invalid_captcha)
			ExceptionLabel.InvalidJWT -> getString(Res.string.error_invalid_jwt)
			ExceptionLabel.InvalidRegistration ->getString(Res.string.error_invalid_registration)
			ExceptionLabel.InvalidRegistrationParameters -> getString(Res.string.error_invalid_registration_data)
			ExceptionLabel.Unauthorized -> getString(Res.string.error_unauthorized)
			null -> getString(Res.string.error_generic)
		}
	}
	else -> getString(Res.string.error_generic)
}.let { ErrorState(it) }
