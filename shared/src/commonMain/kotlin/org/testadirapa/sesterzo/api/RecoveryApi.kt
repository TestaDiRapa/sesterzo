package org.testadirapa.sesterzo.api

import org.testadirapa.sesterzo.model.Timestamp
import org.testadirapa.sesterzo.model.RecoveryKey

interface RecoveryApi {

	/**
	 * Creates a new [RecoveryKey] for the private key of the current user, saves it to the backend and returns the
	 * recovery AES key.
	 *
	 * @param owner the user that will be able to access the key.
	 * @param expiresAt an expiration timestamp for the key. If null the key will have no expiration.
	 * @return the [ByteArray] of the recovery AES key.
	 */
	suspend fun generateRecoveryKey(owner: String, expiresAt: Timestamp?): ByteArray

	/**
	 * Recover the private key stored in the [RecoveryKey] corresponding to the provided AES key bytes and decrypts it.
	 *
	 * @param recoveryKeyBytes the bytes of the AES key used to encrypt the private key.
	 * @return the bytes of the private key.
	 */
	suspend fun recoverKey(recoveryKeyBytes: ByteArray): ByteArray
}