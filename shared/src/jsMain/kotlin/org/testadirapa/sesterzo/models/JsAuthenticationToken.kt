package org.testadirapa.sesterzo.models

import org.testadirapa.sesterzo.model.AuthenticationToken
import org.testadirapa.sesterzo.utils.emptyObject

external interface JsAuthenticationToken {
	var token: String
	var createdAt: Double
	var expiresAt: Double
}

fun AuthenticationToken.toJs(): JsAuthenticationToken {
	val js = emptyObject<JsAuthenticationToken>()
	js.token = token
	js.createdAt = createdAt.toDouble()
	js.expiresAt = expiresAt.toDouble()
	return js
}

fun JsAuthenticationToken.toKt(): AuthenticationToken = AuthenticationToken(
	token = token,
	createdAt = createdAt.toLong(),
	expiresAt = expiresAt.toLong()
)