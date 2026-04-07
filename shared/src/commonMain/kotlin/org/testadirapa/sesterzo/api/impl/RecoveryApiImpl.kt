package org.testadirapa.sesterzo.api.impl

import com.icure.kryptom.crypto.AesAlgorithm.CbcWithPkcs7Padding
import com.icure.kryptom.crypto.defaultCryptoService
import com.icure.kryptom.utils.base64Decode
import com.icure.kryptom.utils.base64Encode
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
import org.testadirapa.sesterzo.model.RecoveryKey
import org.testadirapa.sesterzo.services.AuthService

open class RecoveryApiImpl(
	httpConfig: HttpConfig,
	private val authService: AuthService,
) : AbstractApi(httpConfig), RecoveryApi {

	override val baseSegment: String = "recoveryKey"

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

	override suspend fun recoverKey(
		recoveryKeyBytes: ByteArray,
	): ByteArray {
		val recoveryKey = defaultCryptoService.aes.loadKey(
			algorithm = CbcWithPkcs7Padding,
			bytes = recoveryKeyBytes,
		)
		val recoveryKeyHash = base64Encode(
			defaultCryptoService.digest.sha256(recoveryKeyBytes)
		)
		val encryptedPrivateKey = getRecoveryKey(recoveryKeyId = recoveryKeyHash).bodyOrThrow().encryptedKey.let {
			base64Decode(it)
		}
		return defaultCryptoService.aes.decrypt(
			ivAndEncryptedData = encryptedPrivateKey,
			key = recoveryKey
		)
	}
}