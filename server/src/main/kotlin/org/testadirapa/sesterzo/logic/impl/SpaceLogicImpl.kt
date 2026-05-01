package org.testadirapa.sesterzo.logic.impl

import com.icure.kryptom.crypto.defaultCryptoService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.emitAll
import org.testadirapa.sesterzo.dao.BudgetElementDAO
import org.testadirapa.sesterzo.dao.SpaceDAO
import org.testadirapa.sesterzo.exceptions.EntityNotFoundException
import org.testadirapa.sesterzo.exceptions.ExceptionLabel
import org.testadirapa.sesterzo.exceptions.ImageTooLargeException
import org.testadirapa.sesterzo.exceptions.InvalidSpaceAuthorizationException
import org.testadirapa.sesterzo.exceptions.QuotaExceededException
import org.testadirapa.sesterzo.logic.SpaceLogic
import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.EncryptedBudgetElement
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.model.SpaceStub
import org.testadirapa.sesterzo.security.SecurityContext.Companion.flowOnSecurityContext
import org.testadirapa.sesterzo.security.SecurityContext.Companion.withSecurityContext
import org.testadirapa.sesterzo.utils.isSizeUnderThreshold

class SpaceLogicImpl(
	private val budgetElementDAO: BudgetElementDAO,
	private val spaceDAO: SpaceDAO
) : SpaceLogic {

	override fun getSpaces(): Flow<Space> = flowOnSecurityContext {
		emitAll(
			spaceDAO.getByIds(it.spaces.keys)
		)
	}

	override suspend fun getSpace(spaceId: String): Space = withSecurityContext {
		if (spaceId !in spaces.keys) {
			throw InvalidSpaceAuthorizationException(spaceId)
		}
		spaceDAO.getById(spaceId)
			?: throw EntityNotFoundException(entityId = spaceId, label = ExceptionLabel.SpaceNotFound)
	}

	override suspend fun createSpace(spaceStub: SpaceStub): Space = withSecurityContext {
		if(!spaceStub.picture.isSizeUnderThreshold()) {
			throw ImageTooLargeException()
		}
		val existingUserSpaces = spaceDAO.getByOwner(currentUserId).count()
		if (existingUserSpaces > 5) {
			throw QuotaExceededException()
		}
		val savingsId = budgetElementDAO.save(
			spaceId = spaceStub.id,
			entity = EncryptedBudgetElement(
				budgetElementId = defaultCryptoService.strongRandom.randomUUID(),
				version = 0,
				spaceId = spaceStub.id,
				type = BudgetElement.BudgetElementType.Savings,
				encryptedSelf = null
			)
		).budgetElementId
		val incomeId = budgetElementDAO.save(
			spaceId = spaceStub.id,
			entity = EncryptedBudgetElement(
				budgetElementId = defaultCryptoService.strongRandom.randomUUID(),
				version = 0,
				spaceId = spaceStub.id,
				type = BudgetElement.BudgetElementType.Income,
				encryptedSelf = null
			)
		).budgetElementId
		val expensesId = budgetElementDAO.save(
			spaceId = spaceStub.id,
			entity = EncryptedBudgetElement(
				budgetElementId = defaultCryptoService.strongRandom.randomUUID(),
				version = 0,
				spaceId = spaceStub.id,
				type = BudgetElement.BudgetElementType.FixedExpenses,
				encryptedSelf = null
			)
		).budgetElementId
		budgetElementDAO.initIndexes(spaceId = spaceStub.id)
		spaceDAO.save(
			Space(
				id = spaceStub.id,
				version = 0,
				name = spaceStub.name,
				owner = currentUserId,
				fixedExpensesTemplateId = expensesId,
				incomeSourcesTemplateId = incomeId,
				savingsTemplateId = savingsId,
				users = spaceStub.users,
				picture = spaceStub.picture,
				color = spaceStub.color,
			)
		)
	}

}