package org.testadirapa.sesterzo.logic.impl

import org.testadirapa.sesterzo.dao.RecoveryDAO
import org.testadirapa.sesterzo.exceptions.EntityNotFoundException
import org.testadirapa.sesterzo.exceptions.ExceptionLabel
import org.testadirapa.sesterzo.exceptions.RecoveryKeyExpiredException
import org.testadirapa.sesterzo.logic.RecoveryLogic
import org.testadirapa.sesterzo.model.RecoveryKey
import org.testadirapa.sesterzo.security.SecurityContext.Companion.withSecurityContext

class RecoveryLogicImpl(
	private val recoveryDAO: RecoveryDAO
) : RecoveryLogic {
	override suspend fun createRecoveryKey(recoveryKey: RecoveryKey): RecoveryKey = withSecurityContext {
		recoveryDAO.save(
			recoveryKey.copy(owner = currentUserId)
		)
		return recoveryDAO.getById(recoveryKey.id)
			?: throw IllegalStateException("Recovery key ${recoveryKey.id} was not created")
	}

	override suspend fun getRecoveryKey(recoveryKeyId: String): RecoveryKey = withSecurityContext {
		val result = recoveryDAO.getByIdUser(recoveryKeyId = recoveryKeyId, userId = currentUserId)
		when {
			result == null -> throw EntityNotFoundException(recoveryKeyId, ExceptionLabel.RecoveryKeyNotFound)
			result.expiresAt != null && result.expiresAt!! < System.currentTimeMillis() ->
				throw RecoveryKeyExpiredException(recoveryKeyId)
			else -> result
		}
	}
}