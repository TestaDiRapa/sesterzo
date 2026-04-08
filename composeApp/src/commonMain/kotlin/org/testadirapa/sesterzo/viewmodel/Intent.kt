package org.testadirapa.sesterzo.viewmodel

import org.testadirapa.sesterzo.model.Base32String
import org.testadirapa.sesterzo.model.Base64String

sealed interface Intent {
	data class StartRegistration(val email: String, val name: String) : Intent
	data class StartLogin(val email: String) : Intent
	data class CompleteAuthentication(val token: String) : Intent
	data object ConfirmBackup : Intent
	data class RestoreWithPrivateKey(val privateKey: Base64String) : Intent
	data class RestoreWithRecoveryKey(val recoveryKey: Base32String) : Intent
}