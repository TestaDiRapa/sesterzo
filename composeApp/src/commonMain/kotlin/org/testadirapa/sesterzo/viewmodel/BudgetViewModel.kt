package org.testadirapa.sesterzo.viewmodel

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.exceptions.ExceptionWithMessage
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.DecryptedBudget
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.utils.BudgetReference
import org.testadirapa.sesterzo.utils.currentBudgetReference
import org.testadirapa.sesterzo.utils.toReference
import org.testadirapa.sesterzo.viewmodel.intents.BudgetIntent
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.error_budget_update_failed_bulk

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
			is BudgetIntent.UpdateCurrentBudgetTemplate -> updateCurrentBudgetTemplate(
				type = intent.type,
				budgetElement = intent.budgetElement,
				updateCurrentBudget = intent.updateCurrentBudget,
			)
			is BudgetIntent.UpdateBudgetAmount -> updateBudgetAmount(
				budget = intent.budget,
				newAmounts = intent.newAmounts,
				type = intent.type,
			)
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
				val newNextTemplate = AppCtx.api.budget.getFirstBudgetAfter(
					spaceId = spaceId,
					budgetReference = it.nextBudget.budget.toReference(),
					bypassCache = false
				)?.let { newNextBudget ->
					loadTemplates(newNextBudget).getValue(newNextBudget.id)
				}
				BudgetView(
					currentBudget = it.nextBudget,
					nextBudget = newNextTemplate,
					previousBudget = it.currentBudget
				)
			} else {
				it
			}
		}
	}

	private suspend fun navigateToPreviousBudget() {
		_budgetViewState.updateAndGet {
			if(it?.previousBudget != null) {
				val newPreviousTemplate = AppCtx.api.budget.getFirstBudgetBefore(
					spaceId = spaceId,
					budgetReference = it.previousBudget.budget.toReference(),
					bypassCache = false
				)?.let { newPreviousBudget ->
					loadTemplates(newPreviousBudget).getValue(newPreviousBudget.id)
				}
				BudgetView(
					currentBudget = it.previousBudget,
					nextBudget = it.currentBudget,
					previousBudget = newPreviousTemplate
				)
			} else {
				it
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
		val templates = loadTemplates(
			*listOfNotNull(currentBudget, previousBudget, nextBudget).toTypedArray()
		)
		_budgetViewState.update {
			BudgetView(
				currentBudget = templates.getValue(currentBudget.id),
				previousBudget = previousBudget?.let { templates.getValue(it.id) },
				nextBudget = nextBudget?.let { templates.getValue(it.id) },
			)
		}
	}

	private suspend fun updateCurrentBudgetTemplate(
		type: BudgetElement.BudgetElementType,
		budgetElement: DecryptedBudgetElement,
		updateCurrentBudget: Boolean
	) {
		val result = AppCtx.api.budget.updateBudgetsTemplate(
			spaceId = spaceId,
			startingReference = currentBudgetReference(),
			inclusiveStart = updateCurrentBudget,
			type = type,
			budgetElement = budgetElement,
		)
		result
			.filter { !it.success }
			.map { it.element.id }
			.takeIf { it.isNotEmpty() }
			?.let {
				onError(
					ExceptionWithMessage(
						"${getString(Res.string.error_budget_update_failed_bulk)} ${it.joinToString(", ")}"
					)
				)
			}
		val updatedTemplates = loadTemplates(
			*result.mapNotNull {
				if (it.success) {
					it.element
				} else {
					null
				}
			}.toTypedArray()
		)
		_budgetViewState.update { state ->
			state?.copy(
				previousBudget = state.previousBudget?.let { updatedTemplates[it.budget.id] } ?: state.previousBudget,
				currentBudget = updatedTemplates[state.currentBudget.budget.id]?: state.currentBudget,
				nextBudget = state.nextBudget?.let { updatedTemplates[it.budget.id] } ?: state.nextBudget,
			)
		}
	}

	private suspend fun updateBudgetAmount(
		budget: DecryptedBudget,
		newAmounts: Map<String, Amount>,
		type: Entry.EntryType,
	) {
		when(type) {
			Entry.EntryType.Expense -> budget.copy(fixedExpenses = newAmounts)
			Entry.EntryType.Income -> budget.copy(income = newAmounts)
			Entry.EntryType.Saving -> budget.copy(savings = newAmounts)
		}.also {
			updateBudget(it)
		}
	}

	private suspend fun updateBudget(
		updatedBudget: DecryptedBudget,
	) {
		val result = AppCtx.api.budget.updateBudgetEncryptedFields(
			spaceId = spaceId,
			budget = updatedBudget
		)
		if (result.isSuccess) {
			val updated = result.getOrThrow()
			val updatedTemplate = loadTemplates(updated).getValue(updated.id)
			_budgetViewState.update {
				it?.copy(
					previousBudget = if (it.previousBudget?.budget?.id == updatedBudget.id) updatedTemplate else it.previousBudget,
					currentBudget = if (it.currentBudget.budget.id == updatedBudget.id) updatedTemplate else it.currentBudget,
					nextBudget = if (it.nextBudget?.budget?.id == updatedBudget.id) updatedTemplate else it.nextBudget,
				)
			}
		} else {
			result.exceptionOrNull()?.also { onError(it) }
			_budgetViewState.value?.also {
				if (
					it.previousBudget?.budget?.id == updatedBudget.id ||
					it.currentBudget.budget.id == updatedBudget.id ||
					it.nextBudget?.budget?.id == updatedBudget.id
				) {
					initBudgetView(budgetReference = it.currentBudget.budget.toReference())
				}
			}
		}
	}

	private suspend fun loadTemplates(vararg budgets: DecryptedBudget): Map<String, BudgetWithTemplates> {
		val templateReferences = budgets.flatMap {
			listOf(it.savingsReference, it.incomeReference, it.expensesReference)
		}
		val templates = AppCtx.api.budgetElement.getBugetElements(
			spaceId = spaceId,
			budgetElementReferences = templateReferences,
		).associateBy { it.id }
		return budgets.associate {
			it.id to BudgetWithTemplates(
				budget = it,
				savingsTemplate = templates.getValue(it.savingsReference.toId()),
				expensesTemplate = templates.getValue(it.expensesReference.toId()),
				incomeTemplate = templates.getValue(it.incomeReference.toId())
			)
		}
	}

	data class BudgetView(
		val currentBudget: BudgetWithTemplates,
		val previousBudget: BudgetWithTemplates?,
		val nextBudget: BudgetWithTemplates?,
	)

	data class BudgetWithTemplates(
		val budget: DecryptedBudget,
		val savingsTemplate: DecryptedBudgetElement,
		val incomeTemplate: DecryptedBudgetElement,
		val expensesTemplate: DecryptedBudgetElement,
	)
}