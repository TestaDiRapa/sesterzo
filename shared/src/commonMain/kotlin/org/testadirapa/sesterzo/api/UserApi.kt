package org.testadirapa.sesterzo.api

import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Currency
import org.testadirapa.sesterzo.model.User

interface UserApi {

	suspend fun getCurrentUser(): User

	/**
	 * Sets the [User.publicKey] for the current user. This method will fail if the user has already a public key.
	 */
	suspend fun setPublicKeyForCurrentUser(publicKey: Base64String): User

	suspend fun setBackupConfirmation(): User
	suspend fun getUsers(userIds: List<String>, bypassCache: Boolean): List<User>
	suspend fun setName(name: String): User
	suspend fun setCurrency(currency: Currency): User
	suspend fun setLogsOptIn(optIn: Boolean): User
}