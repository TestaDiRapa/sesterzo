package org.testadirapa.sesterzo.viewmodel

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.utils.currentBudgetReference
import org.testadirapa.sesterzo.utils.nextReference
import org.testadirapa.sesterzo.utils.previousReference
import org.testadirapa.sesterzo.utils.toReference
import org.testadirapa.sesterzo.viewmodel.intents.BudgetIntent

class BudgetViewModel(
	private val spaceId: String,
	private val errorHandler: (e: Throwable) -> Unit,
) : AbstractViewModel<BudgetIntent>() {
	override val logger = Logger.withTag("BudgetViewModel")

	private val _budgetViewState = MutableStateFlow<BudgetView?>(null)
	val budgetViewState: StateFlow<BudgetView?> = _budgetViewState

	init {
		viewModelScope.launch {
			setLoading()
			runCatching {
				initBudgetView()
			}.onFailure(errorHandler)
			unsetLoading()
		}
	}

	override suspend fun processIntent(intent: BudgetIntent) {
		when(intent) {
			BudgetIntent.NavigateToNext -> navigateToNextBudget()
			BudgetIntent.NavigateToPrevious -> navigateToPreviousBudget()
			is BudgetIntent.CreateBudget -> createBudget(reference = intent.newReference)
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
		initBudgetView()
	}

	private suspend fun navigateToNextBudget() {
		_budgetViewState.update {
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
		}
	}

	private suspend fun navigateToPreviousBudget() {
		_budgetViewState.update {
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
		}
	}

	suspend fun initBudgetView() {
		val currentBudgetReference = currentBudgetReference()
		val currentBudget = AppCtx.api.budget.getOrCreateMonthBudget(
			spaceId = spaceId,
			budgetReference = currentBudgetReference,
			bypassCache = false
		)
		val previousBudget = AppCtx.api.budget.getFirstBudgetBefore(
			spaceId = spaceId,
			budgetReference = currentBudgetReference,
			bypassCache = false
		)
		val nextBudget = AppCtx.api.budget.getFirstBudgetAfter(
			spaceId = spaceId,
			budgetReference = currentBudgetReference,
			bypassCache = false
		)
		_budgetViewState.update {
			BudgetView(
				currentBudget = currentBudget,
				previousBudget = previousBudget,
				nextBudget = nextBudget
			)
		}
	}

	data class BudgetView(
		val currentBudget: DecryptedBudget,
		val previousBudget: DecryptedBudget?,
		val nextBudget: DecryptedBudget?,
	)
}