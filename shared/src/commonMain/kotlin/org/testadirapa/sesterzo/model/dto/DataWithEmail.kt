package org.testadirapa.sesterzo.model.dto

import kotlinx.serialization.Serializable

@Serializable
sealed interface DataWithEmail {
	val email: String
}