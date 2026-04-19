package org.testadirapa.sesterzo.viewmodel.errors

import org.jetbrains.compose.resources.getString
import org.testadirapa.sesterzo.exceptions.ExceptionLabel
import org.testadirapa.sesterzo.exceptions.ExceptionWithLabel
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.error_budget_element_not_found
import sesterzo.composeapp.generated.resources.error_budget_not_found
import sesterzo.composeapp.generated.resources.error_cannot_decrypt_aes
import sesterzo.composeapp.generated.resources.error_cannot_decrypt_rsa
import sesterzo.composeapp.generated.resources.error_cannot_encrypt_aes
import sesterzo.composeapp.generated.resources.error_expense_deletion_failed
import sesterzo.composeapp.generated.resources.error_forbidden_in_space
import sesterzo.composeapp.generated.resources.error_generic
import sesterzo.composeapp.generated.resources.error_image_too_large
import sesterzo.composeapp.generated.resources.error_invalid_captcha
import sesterzo.composeapp.generated.resources.error_invalid_jwt
import sesterzo.composeapp.generated.resources.error_invalid_key_format
import sesterzo.composeapp.generated.resources.error_invalid_private_key
import sesterzo.composeapp.generated.resources.error_invalid_registration
import sesterzo.composeapp.generated.resources.error_invalid_registration_data
import sesterzo.composeapp.generated.resources.error_missing_budget_key
import sesterzo.composeapp.generated.resources.error_public_key_update_failed
import sesterzo.composeapp.generated.resources.error_quota_exceeded
import sesterzo.composeapp.generated.resources.error_recovery_key_expired
import sesterzo.composeapp.generated.resources.error_recovery_key_not_found
import sesterzo.composeapp.generated.resources.error_space_not_found
import sesterzo.composeapp.generated.resources.error_unauthorized
import sesterzo.composeapp.generated.resources.error_user_not_found
import sesterzo.composeapp.generated.resources.error_user_update_failed

data class ErrorState(val message: String)

suspend fun Throwable.toErrorState(): ErrorState = when(this) {
	is ExceptionWithLabel -> {
		when (label) {
			null -> getString(Res.string.error_generic)
			ExceptionLabel.InvalidCaptcha -> getString(Res.string.error_invalid_captcha)
			ExceptionLabel.InvalidJwt -> getString(Res.string.error_invalid_jwt)
			ExceptionLabel.InvalidRegistration ->getString(Res.string.error_invalid_registration)
			ExceptionLabel.InvalidRegistrationParameters -> getString(Res.string.error_invalid_registration_data)
			ExceptionLabel.Unauthorized -> getString(Res.string.error_unauthorized)
			ExceptionLabel.PublicKeyUpdateFailed -> getString(Res.string.error_public_key_update_failed)
			ExceptionLabel.RecoveryKeyExpired -> getString(Res.string.error_recovery_key_expired)
			ExceptionLabel.RecoveryKeyNotFound -> getString(Res.string.error_recovery_key_not_found)
			ExceptionLabel.UserNotFound -> getString(Res.string.error_user_not_found)
			ExceptionLabel.UserUpdateFailed -> getString(Res.string.error_user_update_failed)
			ExceptionLabel.InvalidPrivateKey -> getString(Res.string.error_invalid_private_key)
			ExceptionLabel.InvalidKeyFormat -> getString(Res.string.error_invalid_key_format)
			ExceptionLabel.CannotDecryptWithAesKey -> getString(Res.string.error_cannot_decrypt_aes)
			ExceptionLabel.CannotDecryptWithRsaKey -> getString(Res.string.error_cannot_decrypt_rsa)
			ExceptionLabel.ImageTooLarge -> getString(Res.string.error_image_too_large)
			ExceptionLabel.QuotaExceeded -> getString(Res.string.error_quota_exceeded)
			ExceptionLabel.BudgetNotFound -> getString(Res.string.error_budget_not_found)
			ExceptionLabel.CannotEncryptWithAesKey -> getString(Res.string.error_cannot_encrypt_aes)
			ExceptionLabel.ForbiddenInSpace -> getString(Res.string.error_forbidden_in_space)
			ExceptionLabel.MissingSpaceKey -> getString(Res.string.error_missing_budget_key)
			ExceptionLabel.BudgetElementNotFound -> getString(Res.string.error_budget_element_not_found)
			ExceptionLabel.ExpenseDeletionFailed -> getString(Res.string.error_expense_deletion_failed)
			ExceptionLabel.SpaceNotFound -> getString(Res.string.error_space_not_found)
		}
	}
	else -> "${getString(Res.string.error_generic)}${message?.let { ": $it" } ?: ""}"
}.let { ErrorState(it) }
