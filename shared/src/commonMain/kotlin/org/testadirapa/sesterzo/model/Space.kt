package org.testadirapa.sesterzo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Space(
	@SerialName("_id") override val id: String,
	override val version: Int,
	val name: String,
	val owner: String,
	val fixedExpensesTemplateId: String,
	val incomeSourcesTemplateId: String,
	val savingsTemplateId: String,
	val users: Map<String, AccessKey>,
	val picture: Base64String?,
	val color: ULong? = null
) : Versionable
