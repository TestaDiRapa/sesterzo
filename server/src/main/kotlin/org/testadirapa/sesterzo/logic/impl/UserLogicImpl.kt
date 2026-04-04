package org.testadirapa.sesterzo.logic.impl

import org.testadirapa.sesterzo.dao.UserDAO
import org.testadirapa.sesterzo.exceptions.PublicKeyUpdateFailedException
import org.testadirapa.sesterzo.exceptions.UserNotFoundException
import org.testadirapa.sesterzo.logic.UserLogic
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.User
import org.testadirapa.sesterzo.security.SecurityContext.Companion.withSecurityContext

class UserLogicImpl(
	val userDAO: UserDAO,
) : UserLogic {

	override suspend fun getCurrentUser(): User = withSecurityContext {
		userDAO.getById(currentUserId) ?: throw UserNotFoundException(currentUserId)
	}

	override suspend fun setPublicKey(publicKey: Base64String): User = withSecurityContext {
		userDAO.setPublicKey(userId = currentUserId, publicKey = publicKey).let { result ->
			when {
				result == null -> throw UserNotFoundException(currentUserId)
				result.publicKey != publicKey -> throw PublicKeyUpdateFailedException(currentUserId)
				else -> result
			}
		}
	}


}