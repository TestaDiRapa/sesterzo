package org.testadirapa.sesterzo.viewmodel

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.DecryptedEntry
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.viewmodel.intents.EntryIntent

class EntriesViewModel(
	private val spaceId: String,
	private val budget: DecryptedBudget,
	private val errorHandler: (e: Throwable) -> Unit,
) : AbstractViewModel<EntryIntent>() {
	override val logger = Logger.withTag("EntriesViewModel")

	private val _entriesViewState = MutableStateFlow<List<DecryptedEntry>>(emptyList())
	val entriesViewState: StateFlow<List<DecryptedEntry>> = _entriesViewState

	init {
		viewModelScope.launch {
			setLoading()
			runCatching {
				loadEntries(budgetId = budget.id)
			}.onFailure(errorHandler)
			unsetLoading()
		}
	}

	override suspend fun processIntent(intent: EntryIntent) {
		when(intent) {
			is EntryIntent.CreateEntry -> createEntry(
				budgetReference = intent.budgetReference,
				type = intent.type,
				label = intent.label,
				amount = intent.amount,
				description = intent.description
			)
			is EntryIntent.DeleteEntry -> deleteEntry(entryId = intent.entryId)
		}
	}

	override fun onError(error: Throwable) {
		errorHandler(error)
	}

	private suspend fun loadEntries(budgetId: String) {
		_entriesViewState.update {
			AppCtx.api.entry.getInSpaceForBudget(
				spaceId = spaceId,
				budgetId = budgetId,
				bypassCache = false
			)
		}
	}

	private suspend fun createEntry(
		budgetReference: BudgetReference,
		type: Entry.EntryType,
		label: String,
		amount: Amount,
		description: String?
	) {
		AppCtx.api.entry.createEntryAndRetrieve(
			spaceId = spaceId,
			budgetReference = budgetReference,
			type = type,
			label = label,
			amount = amount,
			description = description,
		).also { entries ->
			_entriesViewState.update { entries }
		}
	}

	private suspend fun deleteEntry(entryId: String) {
		AppCtx.api.entry.deleteEntryAndRetrieve(
			spaceId = spaceId,
			entryId = entryId
		).also { entries ->
			_entriesViewState.update { entries }
		}
	}
}