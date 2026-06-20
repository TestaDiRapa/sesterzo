package org.testadirapa.sesterzo.logic.impl

import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.dao.BudgetElementDAO
import org.testadirapa.sesterzo.exceptions.EntityNotFoundException
import org.testadirapa.sesterzo.exceptions.ExceptionLabel
import org.testadirapa.sesterzo.logic.BudgetElementLogic
import org.testadirapa.sesterzo.model.EncryptedBudgetElement
import org.testadirapa.sesterzo.model.Versionable
import org.testadirapa.sesterzo.model.VersionableReference
import org.testadirapa.sesterzo.utils.requireSizeIsUnderThreshold

class BudgetElementLogicImpl(
	private val budgetElementDAO: BudgetElementDAO
) : BudgetElementLogic {

	override suspend fun getLatestVersionForId(spaceId: String, budgetElementId: String): EncryptedBudgetElement =
		budgetElementDAO.getLatestVersionForId(spaceId, budgetElementId)
			?: throw EntityNotFoundException(
				entityId = budgetElementId,
				label = ExceptionLabel.BudgetElementNotFound
			)

	override suspend fun getBudgetElement(spaceId: String, budgetElementId: String, version: Int): EncryptedBudgetElement =
		budgetElementDAO.getById(spaceId = spaceId, id = "$budgetElementId-$version")
			?: throw EntityNotFoundException(
				entityId = Versionable.idOf(entityId = budgetElementId, version = version),
				label = ExceptionLabel.BudgetElementNotFound
			)

	override fun getBudgetElements(
		spaceId: String,
		budgetElementIds: List<String>
	): Flow<EncryptedBudgetElement> = budgetElementDAO.getByIds(
		spaceId = spaceId,
		ids = budgetElementIds.toSet()
	)

	override suspend fun createBudgetElement(
		spaceId: String,
		budgetElement: EncryptedBudgetElement
	): EncryptedBudgetElement {
		budgetElement.requireSizeIsUnderThreshold()
		return budgetElementDAO.save(spaceId = spaceId, entity = budgetElement)
	}

}