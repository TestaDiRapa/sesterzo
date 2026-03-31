package org.testadirapa.sesterzo.security

object NoOpBiometricAuthenticator : BiometricAuthenticator {
	override fun authenticate(
		title: String,
		subtitle: String,
		description: String,
		onSuccess: () -> Unit,
		onFailure: (String) -> Unit
	) {
		onSuccess()
	}
}