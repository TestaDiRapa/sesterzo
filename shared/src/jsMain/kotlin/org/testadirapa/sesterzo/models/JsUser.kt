package org.testadirapa.sesterzo.models

import org.testadirapa.sesterzo.cache.model.CachedUser
import org.testadirapa.sesterzo.model.Currency
import org.testadirapa.sesterzo.model.User
import org.testadirapa.sesterzo.utils.currentTimeMillis
import org.testadirapa.sesterzo.utils.emptyObject
import org.testadirapa.sesterzo.utils.mapToObject
import org.testadirapa.sesterzo.utils.objectToMap

@OptIn(ExperimentalWasmJsInterop::class)
external interface JsUser : JsAny {
	var id: String
	var name: String
	var email: String
	var authenticationTokens: Record<String, JsAuthenticationToken>
	var publicKey: String?
	var hasBackup: Boolean
	var preferredCurrency: String
	var insertedAt: Double
	var sendLogs: Boolean
}

fun User.toJs(insertedAt: Double = currentTimeMillis()): JsUser {
	val js = emptyObject<JsUser>()
	js.id = id
	js.name = name
	js.email = email
	js.authenticationTokens = mapToObject(authenticationTokens) { it.toJs() }
	js.publicKey = publicKey
	js.hasBackup = hasBackup
	js.preferredCurrency = preferredCurrency.name
	js.insertedAt = insertedAt
	js.sendLogs = sendLogs
	return js
}

fun JsUser.toKt(): CachedUser = CachedUser(
	id = id,
	entity = User(
		id = id,
		name = name,
		email = email,
		authenticationTokens = objectToMap(authenticationTokens) { it.toKt() },
		publicKey = publicKey,
		preferredCurrency = Currency.valueOf(preferredCurrency),
		hasBackup = hasBackup,
		sendLogs = sendLogs,
	),
	insertedAt = insertedAt.toLong()
)