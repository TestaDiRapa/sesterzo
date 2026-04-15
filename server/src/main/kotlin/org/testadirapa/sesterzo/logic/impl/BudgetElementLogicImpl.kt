package org.testadirapa.sesterzo.logic.impl

import org.testadirapa.sesterzo.dao.BudgetElementDAO
import org.testadirapa.sesterzo.exceptions.EntityNotFoundException
import org.testadirapa.sesterzo.exceptions.ExceptionLabel
import org.testadirapa.sesterzo.logic.BudgetElementLogic
import org.testadirapa.sesterzo.model.EncryptedBudgetElement

class BudgetElementLogicImpl(
	private val budgetElementDAO: BudgetElementDAO
) : BudgetElementLogic {

	override suspend fun getLatestVersionForId(spaceId: String, budgetElementId: String): EncryptedBudgetElement =
		budgetElementDAO.getLatestVersionForId(spaceId, budgetElementId)
			?: throw EntityNotFoundException(
				entityId = budgetElementId,
				label = ExceptionLabel.BudgetElementNotFound
			)

}