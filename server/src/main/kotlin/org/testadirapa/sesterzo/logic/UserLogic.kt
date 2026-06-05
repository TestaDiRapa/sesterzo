package org.testadirapa.sesterzo.logic

import kotlinx.coroutines.flow.Flow
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Currency
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
	fun getUsers(userIds: Set<String>): Flow<User>
	suspend fun setName(name: String): User
	suspend fun setCurrency(currency: Currency): User
}