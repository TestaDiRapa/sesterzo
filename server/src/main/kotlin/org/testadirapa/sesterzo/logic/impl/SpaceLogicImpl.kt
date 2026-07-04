package org.testadirapa.sesterzo.logic.impl

import com.icure.kryptom.crypto.defaultCryptoService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.emitAll
import org.testadirapa.sesterzo.dao.AttachmentDAO
import org.testadirapa.sesterzo.dao.BudgetElementDAO
import org.testadirapa.sesterzo.dao.SpaceDAO
import org.testadirapa.sesterzo.dao.UserDAO
import org.testadirapa.sesterzo.exceptions.EntityNotFoundException
import org.testadirapa.sesterzo.exceptions.ExceptionLabel
import org.testadirapa.sesterzo.exceptions.ImageTooLargeException
import org.testadirapa.sesterzo.exceptions.InvalidSpaceAuthorizationException
import org.testadirapa.sesterzo.exceptions.QuotaExceededException
import org.testadirapa.sesterzo.exceptions.SpacePictureUpdateFailed
import org.testadirapa.sesterzo.logic.SpaceLogic
import org.testadirapa.sesterzo.model.BudgetElement
import org.testadirapa.sesterzo.model.EncryptedAttachment
import org.testadirapa.sesterzo.model.EncryptedBudgetElement
import org.testadirapa.sesterzo.model.RGBColor
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.model.SpaceStub
import org.testadirapa.sesterzo.model.User
import org.testadirapa.sesterzo.security.SecurityContext.Companion.flowOnSecurityContext
import org.testadirapa.sesterzo.security.SecurityContext.Companion.withSecurityContext
import org.testadirapa.sesterzo.utils.isSizeUnderThreshold
import org.testadirapa.sesterzo.validators.defaultNameValidator

class SpaceLogicImpl(
	private val attachmentDAO: AttachmentDAO,
	private val budgetElementDAO: BudgetElementDAO,
	private val spaceDAO: SpaceDAO,
	private val userDAO: UserDAO,
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

	override suspend fun setSpacePicture(spaceId: String, picture: EncryptedAttachment): Space = withSecurityContext {
		if (spaceId !in spaces.keys) {
			throw InvalidSpaceAuthorizationException(spaceId)
		}
		if(!picture.encryptedSelf.isSizeUnderThreshold()) {
			throw ImageTooLargeException()
		}
		val imageRef = attachmentDAO.save(
			spaceId = spaceId,
			entity = picture
		).id
		return spaceDAO.setSpaceThumbnail(
			spaceId = spaceId,
			pictureRef = imageRef
		) ?: throw SpacePictureUpdateFailed(spaceId = spaceId)
	}

	override suspend fun createSpace(spaceStub: SpaceStub): Space = withSecurityContext {
		val existingUserSpaces = spaceDAO.getByParticipant(currentUserId).count()
		if (existingUserSpaces > 5) {
			throw QuotaExceededException()
		}
		if (!defaultNameValidator.isValid(spaceStub.name)) {
			throw IllegalArgumentException("Invalid space name: ${spaceStub.name}")
		}
		val user = userDAO.getById(currentUserId)
			?: throw EntityNotFoundException(entityId = currentUserId, label = ExceptionLabel.UserNotFound)
		val spaceId = "${user.getEmailFingerprint()}-${spaceStub.id}"
		val savingsId = budgetElementDAO.save(
			spaceId = spaceId,
			entity = EncryptedBudgetElement(
				budgetElementId = defaultCryptoService.strongRandom.randomUUID(),
				version = 0,
				spaceId = spaceId,
				type = BudgetElement.BudgetElementType.Savings,
				encryptedSelf = null
			)
		).budgetElementId
		val incomeId = budgetElementDAO.save(
			spaceId = spaceId,
			entity = EncryptedBudgetElement(
				budgetElementId = defaultCryptoService.strongRandom.randomUUID(),
				version = 0,
				spaceId = spaceId,
				type = BudgetElement.BudgetElementType.Income,
				encryptedSelf = null
			)
		).budgetElementId
		val expensesId = budgetElementDAO.save(
			spaceId = spaceId,
			entity = EncryptedBudgetElement(
				budgetElementId = defaultCryptoService.strongRandom.randomUUID(),
				version = 0,
				spaceId = spaceId,
				type = BudgetElement.BudgetElementType.FixedExpenses,
				encryptedSelf = null
			)
		).budgetElementId
		budgetElementDAO.initIndexes(spaceId = spaceId)
		spaceDAO.save(
			Space(
				id = spaceId,
				version = 0,
				name = spaceStub.name,
				owner = currentUserId,
				fixedExpensesTemplateId = expensesId,
				incomeSourcesTemplateId = incomeId,
				savingsTemplateId = savingsId,
				users = spaceStub.users,
				pictureReference = null,
				color = spaceStub.color,
			)
		)
	}

	private fun User.getEmailFingerprint(): String =
		email.split('@').first().filter {
			it in 'a'..'z' || it in 'A'..'Z' || it in '0'..'9'
		}.take(10).lowercase()

	override suspend fun setSpaceNameAndColor(spaceId: String, name: String, color: RGBColor): Space = withSecurityContext {
		if (spaceId !in spaces.keys) {
			throw InvalidSpaceAuthorizationException(spaceId)
		}
		if (!defaultNameValidator.isValid(name)) {
			throw IllegalArgumentException("Invalid space name: $name")
		}
		spaceDAO.setSpaceNameAndColor(
			spaceId = spaceId,
			name = name,
			color = color
		) ?: throw EntityNotFoundException(entityId = spaceId, label = ExceptionLabel.SpaceNotFound)
	}

}