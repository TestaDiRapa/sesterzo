package org.testadirapa.sesterzo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
	@SerialName("_id") override val id: String,
	val name: String,
	val email: String,
	val authenticationTokens: Map<String, AuthenticationToken> = emptyMap(),
	val publicKey: Base64String? = null,
	val hasBackup: Boolean = false,
	val preferredCurrency: Currency = Currency.EUR,
) : Identifiable