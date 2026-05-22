package org.testadirapa.sesterzo.viewmodel

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.DecryptedEntry
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.utils.currentBudgetReference
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.utils.toReference
import org.testadirapa.sesterzo.viewmodel.intents.BudgetIntent

class BudgetViewModel(
	private val spaceId: String,
	private val errorHandler: (e: Throwable) -> Unit,
) : AbstractViewModel<BudgetIntent>() {
	override val logger = Logger.withTag("BudgetViewModel")

	private val _budgetViewState = MutableStateFlow<BudgetView?>(null)
	val budgetViewState: StateFlow<BudgetView?> = _budgetViewState

	private val _entriesViewState = MutableStateFlow<List<DecryptedEntry>>(emptyList())
	val entriesViewState: StateFlow<List<DecryptedEntry>> = _entriesViewState

	init {
		viewModelScope.launch {
			setLoading()
			runCatching {
				initBudgetView(currentBudgetReference())
			}.onFailure(errorHandler)
			unsetLoading()
		}
	}

	override suspend fun processIntent(intent: BudgetIntent) {
		when(intent) {
			BudgetIntent.NavigateToNext -> navigateToNextBudget()
			BudgetIntent.NavigateToPrevious -> navigateToPreviousBudget()
			is BudgetIntent.NavigateTo -> initBudgetView(intent.budgetReference)
			is BudgetIntent.CreateBudget -> createBudget(reference = intent.newReference)
			is BudgetIntent.CreateEntry -> createEntry(
				budgetReference = intent.budgetReference,
				type = intent.type,
				label = intent.label,
				amount = intent.amount,
				description = intent.description
			)
			is BudgetIntent.DeleteEntry -> deleteEntry(entryId = intent.entryId)
		}
	}

	override fun onError(error: Throwable) {
		errorHandler(error)
	}

	private suspend fun createBudget(reference: BudgetReference) {
		AppCtx.api.budget.createBudget(
			spaceId = spaceId,
			budgetReference = reference,
		)
		initBudgetView(currentBudgetReference())
	}

	private suspend fun navigateToNextBudget() {
		_budgetViewState.updateAndGet {
			if(it?.nextBudget != null) {
				val newNextBudget = AppCtx.api.budget.getFirstBudgetAfter(
					spaceId = spaceId,
					budgetReference = it.nextBudget.toReference(),
					bypassCache = false
				)
				BudgetView(
					currentBudget = it.nextBudget,
					nextBudget = newNextBudget,
					previousBudget = it.currentBudget
				)
			} else {
				it
			}
		}.also {
			if (it != null) {
				loadEntries(budgetId = it.currentBudget.id)
			}
		}
	}

	private suspend fun navigateToPreviousBudget() {
		_budgetViewState.updateAndGet {
			if(it?.previousBudget != null) {
				val newPreviousBudget = AppCtx.api.budget.getFirstBudgetBefore(
					spaceId = spaceId,
					budgetReference = it.previousBudget.toReference(),
					bypassCache = false
				)
				BudgetView(
					currentBudget = it.previousBudget,
					nextBudget = it.currentBudget,
					previousBudget = newPreviousBudget
				)
			} else {
				it
			}
		}.also {
			if (it != null) {
				loadEntries(budgetId = it.currentBudget.id)
			}
		}
	}

	private suspend fun initBudgetView(budgetReference: BudgetReference) {
		val currentBudget = AppCtx.api.budget.getOrCreateMonthBudget(
			spaceId = spaceId,
			budgetReference = budgetReference,
			bypassCache = false
		)
		val previousBudget = AppCtx.api.budget.getFirstBudgetBefore(
			spaceId = spaceId,
			budgetReference = budgetReference,
			bypassCache = false
		)
		val nextBudget = AppCtx.api.budget.getFirstBudgetAfter(
			spaceId = spaceId,
			budgetReference = budgetReference,
			bypassCache = false
		)
		_budgetViewState.update {
			BudgetView(
				currentBudget = currentBudget,
				previousBudget = previousBudget,
				nextBudget = nextBudget
			)
		}
		loadEntries(budgetId = currentBudget.id)
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

	data class BudgetView(
		val currentBudget: DecryptedBudget,
		val previousBudget: DecryptedBudget?,
		val nextBudget: DecryptedBudget?,
	)
}