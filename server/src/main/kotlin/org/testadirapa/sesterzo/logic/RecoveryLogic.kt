package org.testadirapa.sesterzo.logic

import org.testadirapa.sesterzo.model.RecoveryKey
import org.testadirapa.sesterzo.exceptions.EntityNotFoundException
import org.testadirapa.sesterzo.exceptions.RecoveryKeyExpiredException

interface RecoveryLogic {

	suspend fun createRecoveryKey(recoveryKey: RecoveryKey): RecoveryKey

	/**
	 * Retrieves a recovery key by id with the current user id as [RecoveryKey.owner].
	 *
	 * @param recoveryKeyId the id of the recovery key.
	 * @return a [RecoveryKey] if one was found, and it was not expired.
	 * @throws EntityNotFoundException if no recovery key for the user was found.
	 * @throws RecoveryKeyExpiredException if the recovery key expired.
	 */
	suspend fun getRecoveryKey(recoveryKeyId: String): RecoveryKey

}