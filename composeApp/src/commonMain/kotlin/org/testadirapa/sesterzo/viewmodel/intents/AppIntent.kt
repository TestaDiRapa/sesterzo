package org.testadirapa.sesterzo.viewmodel.intents

import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Bip39RecoveryKey

sealed interface AppIntent : Intent {
	data class StartRegistration(val email: String, val name: String) : AppIntent
	data class StartLogin(val email: String) : AppIntent
	data class CompleteAuthentication(val token: String) : AppIntent
	data object ConfirmBackup : AppIntent
	data class RestoreWithPrivateKey(val privateKey: Base64String) : AppIntent
	data class RestoreWithRecoveryKey(val recoveryKey: Bip39RecoveryKey) : AppIntent
	data class CreateFirstSpaceIntent(val name: String, val picture: ByteArray?) : AppIntent
}