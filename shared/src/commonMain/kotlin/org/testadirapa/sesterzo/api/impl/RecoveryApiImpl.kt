package org.testadirapa.sesterzo.api.impl

import com.icure.kryptom.crypto.defaultCryptoService
import com.icure.kryptom.utils.base64UrlEncode
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.ContentType.Application
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.util.date.GMTDate
import org.testadirapa.sesterzo.api.RecoveryApi
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.http.wrap
import org.testadirapa.sesterzo.model.Bip39RecoveryKey
import org.testadirapa.sesterzo.model.RecoveryKey
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.utils.BitArray
import org.testadirapa.sesterzo.utils.aesDecrypt
import org.testadirapa.sesterzo.utils.decodeBase64Key
import org.testadirapa.sesterzo.utils.loadAesKey

open class RecoveryApiImpl(
	httpConfig: HttpConfig,
	private val authService: AuthService,
) : AbstractApi(httpConfig), RecoveryApi {

	override val baseSegment: String = "recoveryKey"
	private var bip39WordList: List<String>? = null

	protected suspend fun getRecoveryKey(recoveryKeyId: String): HttpResponse<RecoveryKey> = get {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, recoveryKeyId)
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrap()

	protected suspend fun createRecoveryKey(recoveryKey: RecoveryKey): HttpResponse<RecoveryKey> = post {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment)
			parameter("ts", GMTDate().timestamp)
		}
		contentType(Application.Json)
		setBody(recoveryKey)
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrap()

	protected suspend fun getBip39WordList(): HttpResponse<List<String>> = get {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "bip39")
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrap()

	protected suspend fun getOrLoadWordList(): List<String> {
		if (bip39WordList == null) {
			bip39WordList = getBip39WordList().bodyOrThrow()
		}
		return bip39WordList!!
	}

	protected suspend fun bip39Encode(key: ByteArray): Bip39RecoveryKey {
		require(key.size == 32) { "Must be 256 bits (32 bytes)" }
		val wordList = getOrLoadWordList()
		val hash = defaultCryptoService.digest.sha256(key)
		val checksumByte = hash[0]

		val bits = BitArray(264)
		for (i in key.indices) {
			bits.setOctet(i, key[i])
		}
		bits.setOctet(32, checksumByte)
		return Bip39RecoveryKey(
			words = List(24) { i ->
				val index = bits.getInt(i * 11, 11)
				wordList[index]
			}
		)
	}

	protected suspend fun bip39Decode(key: Bip39RecoveryKey): ByteArray{
		val wordIndex = getOrLoadWordList().mapIndexed { i, w -> w to i }.toMap()
		val bits = BitArray(264)
		key.words.forEachIndexed { i, word ->
			val index = wordIndex[word.lowercase()] ?: error("Unknown word: $word")
			bits.setInt(i * 11, 11, index)
		}
		val entropy = ByteArray(32) { i -> bits.getOctet(i) }
		val hash = defaultCryptoService.digest.sha256(entropy)
		val expectedChecksum = (hash[0].toInt() and 0xFF) shr 0
		val actualChecksum = bits.getInt(256, 8)
		check(expectedChecksum == actualChecksum) {
			"Checksum mismatch — mnemonic is invalid or corrupted"
		}
		return entropy
	}

	override suspend fun recoverKey(
		bip39RecoveryKey: Bip39RecoveryKey
	): ByteArray {
		val recoveryKeyBytes = bip39Decode(bip39RecoveryKey)
		val recoveryKey = loadAesKey(recoveryKeyBytes)
		val recoveryKeyHash = base64UrlEncode(
			defaultCryptoService.digest.sha256(recoveryKeyBytes)
		)
		val encryptedPrivateKey = getRecoveryKey(recoveryKeyId = recoveryKeyHash).bodyOrThrow().encryptedKey.let {
			decodeBase64Key(it)
		}
		return aesDecrypt(encryptedData = encryptedPrivateKey, key = recoveryKey)
	}
}