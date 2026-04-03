package org.testadirapa.sesterzo.viewmodel

sealed interface Intent {
	data class StartRegistration(val email: String, val name: String) : Intent
	data class CompleteAuthentication(val token: String) : Intent
}