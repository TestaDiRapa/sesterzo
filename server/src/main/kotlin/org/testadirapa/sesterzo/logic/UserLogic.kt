package org.testadirapa.sesterzo.logic

import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.User
import org.testadirapa.sesterzo.security.SecurityContext

interface UserLogic {

	/**
	 * Retrieves the current user based on the id retrieved from the [SecurityContext]
	 */
	suspend fun getCurrentUser(): User

	/**
	 * Sets the [User.publicKey] for the current user, if not set already.
	 */
	suspend fun setPublicKey(publicKey: Base64String): User

	/**
	 * Sets [User.hasBackup] to true for the current user.
	 */
	suspend fun setBackupConfirmation(): User
}