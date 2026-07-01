package org.testadirapa.sesterzo.viewmodel.components

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.DecryptedBudgetElement
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.viewmodel.AbstractViewModel
import org.testadirapa.sesterzo.viewmodel.intents.Intent

class TemplateScreenViewModel(
	private val space: Space,
	private val errorHandler: (Throwable) -> Unit,
) : AbstractViewModel<TemplateScreenViewModel.TemplateScreenIntent>() {
	override val logger = Logger.withTag("TemplateScreenViewModel")

	private val _templatesStates = MutableStateFlow<Templates?>(null)
	val templatesStates: StateFlow<Templates?> = _templatesStates

	init {
		viewModelScope.launch {
			processIntent(TemplateScreenIntent.RetrieveTemplates)
		}
	}

	override suspend fun processIntent(intent: TemplateScreenIntent) {
		when (intent) {
			TemplateScreenIntent.RetrieveTemplates -> {
				_templatesStates.update {
					retrieveLatestTemplates()
				}
			}
			is TemplateScreenIntent.UpdateTemplate -> {
				val updatedElement = AppCtx.api.budgetElement.createBudgetElement(
					spaceId = space.id,
					budgetElement = intent.entity.copy(
						version = intent.entity.version + 1,
						elements = intent.updatedAmounts
					)
				)
				intent.onUpdateBudgetsTemplate(updatedElement.type, updatedElement, intent.updatedCurrentBudget)
				_templatesStates.update {
					retrieveLatestTemplates()
				}
			}
		}
	}

	override fun onError(error: Throwable) {
		errorHandler(error)
	}

	sealed interface TemplateScreenIntent : Intent {
		data object RetrieveTemplates : TemplateScreenIntent
		data class UpdateTemplate(
			val entity: DecryptedBudgetElement,
			val updatedAmounts: Map<String, Amount>,
			val updatedCurrentBudget: Boolean,
			val onUpdateBudgetsTemplate: (type: BudgetElement.BudgetElementType, budgetElement: DecryptedBudgetElement, updateCurrent: Boolean) -> Unit
		) : TemplateScreenIntent
	}

	data class Templates(
		val expensesTemplate: DecryptedBudgetElement,
		val savingsTemplate: DecryptedBudgetElement,
		val incomesTemplate: DecryptedBudgetElement,
	)

	private suspend fun retrieveLatestTemplates(): Templates {
		val expensesTemplate = AppCtx.api.budgetElement.getLatestBudgetElementById(
			spaceId = space.id,
			budgetElementId = space.fixedExpensesTemplateId
		)
		val savingsTemplate = AppCtx.api.budgetElement.getLatestBudgetElementById(
			spaceId = space.id,
			budgetElementId = space.savingsTemplateId
		)
		val incomesTemplate = AppCtx.api.budgetElement.getLatestBudgetElementById(
			spaceId = space.id,
			budgetElementId = space.incomeSourcesTemplateId
		)
		return Templates(
			expensesTemplate = expensesTemplate,
			savingsTemplate = savingsTemplate,
			incomesTemplate = incomesTemplate,
		)
	}
}