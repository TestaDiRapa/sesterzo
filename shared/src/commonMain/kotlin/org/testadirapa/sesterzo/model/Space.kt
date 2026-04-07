package org.testadirapa.sesterzo.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Space(
	@SerialName("_id") override val id: String,
	val fixedExpensesTemplateId: String,
	val incomeSourcesTemplateId: String,
	val savingsTemplateId: String,
	val users: Map<String, AccessKey>
) : Identifiable
