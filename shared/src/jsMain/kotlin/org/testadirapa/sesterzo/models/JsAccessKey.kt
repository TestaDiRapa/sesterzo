package org.testadirapa.sesterzo.models

import org.testadirapa.sesterzo.model.AccessKey
import org.testadirapa.sesterzo.model.SharedAccessKey
import org.testadirapa.sesterzo.model.UserAccessKey
import org.testadirapa.sesterzo.serialization.Serialization
import org.testadirapa.sesterzo.utils.emptyObject

sealed external interface JsAccessKey {
	var type: String
	var accessLevel: String?
	var issuedAt: Double?
	var expiresAt: Double?
	var encryptedKey: String
}

fun AccessKey.toJs(): JsAccessKey = when (this) {
	is SharedAccessKey -> {
		val js = emptyObject<JsAccessKey>()
		js.type = "org.testadirapa.sesterzo.model.SharedAccessKey"
		js.issuedAt = issuedAt.toDouble()
		js.expiresAt = expiresAt.toDouble()
		js.encryptedKey = encryptedKey
		js.accessLevel = null
		js
	}
	is UserAccessKey -> {
		val js = emptyObject<JsAccessKey>()
		js.type = "org.testadirapa.sesterzo.model.UserAccessKey"
		js.accessLevel = Serialization.json.encodeToString(accessLevel)
		js.encryptedKey = encryptedKey
		js.issuedAt = null
		js.expiresAt = null
		js
	}
}

fun JsAccessKey.toKt(): AccessKey = when (type) {
	"org.testadirapa.sesterzo.model.SharedAccessKey" -> SharedAccessKey(
		issuedAt = checkNotNull(issuedAt?.toLong()),
		expiresAt = checkNotNull(expiresAt?.toLong()),
		encryptedKey = encryptedKey
	)
	"org.testadirapa.sesterzo.model.UserAccessKey" -> UserAccessKey(
		accessLevel = Serialization.json.decodeFromString(checkNotNull(accessLevel)),
		encryptedKey = encryptedKey
	)
	else -> throw IllegalArgumentException("Unknown access key type $type")
}