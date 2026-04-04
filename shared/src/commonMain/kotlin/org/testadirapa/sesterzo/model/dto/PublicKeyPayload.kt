package org.testadirapa.sesterzo.model.dto

import kotlinx.serialization.Serializable
import org.testadirapa.sesterzo.model.Base64String

@Serializable
data class PublicKeyPayload(val publicKey: Base64String)
