package org.testadirapa.sesterzo.api

import org.testadirapa.sesterzo.model.Bip39RecoveryKey
import org.testadirapa.sesterzo.model.Timestamp
import org.testadirapa.sesterzo.model.RecoveryKey

interface RecoveryApi {

	/**
	 * Recover the private key stored in the [RecoveryKey] corresponding to the provided AES key bytes and decrypts it.
	 *
	 * @param bip39RecoveryKey a [Bip39RecoveryKey].
	 * @return the bytes of the private key.
	 */
	suspend fun recoverKey(bip39RecoveryKey: Bip39RecoveryKey): ByteArray
}

interface FullRecoveryApi : RecoveryApi {

	/**
	 * Creates a new [RecoveryKey] for the private key of the current user, saves it to the backend and returns the
	 * recovery AES key.
	 *
	 * @param receiver the user that will be able to access the key.
	 * @param expiresAt an expiration timestamp for the key. If null the key will have no expiration.
	 * @return a [Bip39RecoveryKey].
	 */
	suspend fun generateRecoveryKey(receiver: String?, expiresAt: Timestamp?): Bip39RecoveryKey

	suspend fun generateRecoveryKeyAndReturnBipIndexes(receiver: String?, expiresAt: Timestamp?): List<Int>

}