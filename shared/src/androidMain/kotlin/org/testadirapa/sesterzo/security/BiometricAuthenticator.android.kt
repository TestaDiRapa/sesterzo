package org.testadirapa.sesterzo.security

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import org.testadirapa.sesterzo.storage.SecureKeyAccessLevel
import kotlin.time.Clock
import java.util.concurrent.Executor
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class AndroidBiometricAuthenticator(
	private val context: Context,
	private val highestAuthAvailable: SecureKeyAccessLevel?
) : BiometricAuthenticator {

	companion object {
		val DURATION_BETWEEN_TWO_BIOMETRIC_PROMPTS = 2.minutes
	}

	private var lastSuccessfulAuthenticationTime: Instant? = null

	override fun authenticate(
		title: String,
		subtitle: String?,
		description: String?,
		onSuccess: () -> Unit,
		onFailure: (String) -> Unit
	) {
		val lastAuthenticationTimeDuration = lastSuccessfulAuthenticationTime?.let {
			it - Clock.System.now()
		}?.absoluteValue

		if (highestAuthAvailable == null || (lastAuthenticationTimeDuration != null && lastAuthenticationTimeDuration < DURATION_BETWEEN_TWO_BIOMETRIC_PROMPTS)) {
			onSuccess()
			return
		}

		val executor: Executor = ContextCompat.getMainExecutor(context)

		val biometricPrompt = BiometricPrompt(
			context as FragmentActivity,
			executor,
			object : BiometricPrompt.AuthenticationCallback() {
				override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
					lastSuccessfulAuthenticationTime = Clock.System.now()
					onSuccess()
				}

				override fun onAuthenticationFailed() {
					onFailure("Authentication failed")
				}

				override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
					onFailure(errString.toString())
				}
			}
		)

		val promptInfo = BiometricPrompt.PromptInfo.Builder()
			.setTitle(title)
			.setSubtitle(subtitle)
			.setDescription(description)
			.setConfirmationRequired(true)
			.setAllowedAuthenticators(BiometricManager.Authenticators.DEVICE_CREDENTIAL or BiometricManager.Authenticators.BIOMETRIC_STRONG)
			.build()

		biometricPrompt.authenticate(promptInfo)
	}
}