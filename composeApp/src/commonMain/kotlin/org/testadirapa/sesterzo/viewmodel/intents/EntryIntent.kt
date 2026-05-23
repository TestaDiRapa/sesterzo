package org.testadirapa.sesterzo.viewmodel.intents

import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.utils.BudgetReference

sealed interface EntryIntent : Intent {
	data class CreateEntry(
		val budgetReference: BudgetReference,
		val type: Entry.EntryType,
		val label: String,
		val amount: Amount,
		val description: String?
	): EntryIntent
	data class DeleteEntry(val entryId: String): EntryIntent
}