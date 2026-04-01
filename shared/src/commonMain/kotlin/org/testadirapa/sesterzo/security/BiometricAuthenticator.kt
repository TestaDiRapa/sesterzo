package org.testadirapa.sesterzo.security

import kotlinx.coroutines.CompletableDeferred

interface BiometricAuthenticator {
	fun authenticate(
		title: String,
		subtitle: String?,
		description: String?,
		onSuccess: () -> Unit,
		onFailure: (String) -> Unit
	)
}

suspend fun BiometricAuthenticator.authenticateUsingBiometric(title: String): Boolean {
	val result = CompletableDeferred<Boolean>()
	this.authenticate(
		title = title,
		subtitle = null,
		description = null,
		onSuccess = {
			result.complete(true)
		},
		onFailure = {
			result.complete(false)
		}
	)

	return result.await()
}