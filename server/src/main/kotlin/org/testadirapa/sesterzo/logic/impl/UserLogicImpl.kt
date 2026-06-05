package org.testadirapa.sesterzo.logic.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.any
import kotlinx.coroutines.flow.emitAll
import org.testadirapa.sesterzo.dao.SpaceDAO
import org.testadirapa.sesterzo.dao.UserDAO
import org.testadirapa.sesterzo.exceptions.PublicKeyUpdateFailedException
import org.testadirapa.sesterzo.exceptions.EntityNotFoundException
import org.testadirapa.sesterzo.exceptions.EntityUpdateFailedException
import org.testadirapa.sesterzo.exceptions.ExceptionLabel
import org.testadirapa.sesterzo.exceptions.InvalidRegistrationParametersException
import org.testadirapa.sesterzo.logic.UserLogic
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Currency
import org.testadirapa.sesterzo.model.User
import org.testadirapa.sesterzo.security.SecurityContext.Companion.flowOnSecurityContext
import org.testadirapa.sesterzo.security.SecurityContext.Companion.withSecurityContext
import org.testadirapa.sesterzo.validators.defaultNameValidator

class UserLogicImpl(
	val userDAO: UserDAO,
	val spaceDAO: SpaceDAO,
) : UserLogic {

	override suspend fun getCurrentUser(): User = withSecurityContext {
		userDAO.getById(currentUserId) ?: throw EntityNotFoundException(currentUserId, ExceptionLabel.UserNotFound)
	}

	override suspend fun setPublicKey(publicKey: Base64String): User = withSecurityContext {
		userDAO.setPublicKey(userId = currentUserId, publicKey = publicKey).let { result ->
			when {
				result == null -> throw EntityNotFoundException(currentUserId, ExceptionLabel.UserNotFound)
				result.publicKey != publicKey -> throw PublicKeyUpdateFailedException(currentUserId)
				else -> result
			}
		}
	}

	override suspend fun setBackupConfirmation(): User = withSecurityContext {
		userDAO.setBackupConfirmation(userId = currentUserId).let { result ->
			when {
				result == null -> throw EntityNotFoundException(currentUserId, ExceptionLabel.UserNotFound)
				!result.hasBackup -> throw EntityUpdateFailedException(currentUserId, ExceptionLabel.UserUpdateFailed)
				else -> result
			}
		}
	}

	override suspend fun setName(name: String): User = withSecurityContext {
		if (!defaultNameValidator.isValid(name)) {
			throw InvalidRegistrationParametersException()
		}
		userDAO.setName(
			userId = currentUserId,
			name = name
		) ?: throw EntityUpdateFailedException(currentUserId, ExceptionLabel.UserUpdateFailed)
	}

	override suspend fun setCurrency(currency: Currency): User = withSecurityContext {
		userDAO.setCurrency(
			userId = currentUserId,
			currency = currency
		) ?: throw EntityUpdateFailedException(currentUserId, ExceptionLabel.UserUpdateFailed)
	}

	override fun getUsers(userIds: Set<String>): Flow<User> = flowOnSecurityContext { ctx ->
		val userSpaces = spaceDAO.getByIds(ctx.spaces.keys)
		val allowedUserIds = userIds.filter { userId ->
			userSpaces.any { it.users.containsKey(userId) }
		}.toSet()
		emitAll(
			userDAO.getByIds(allowedUserIds)
		)
	}

}