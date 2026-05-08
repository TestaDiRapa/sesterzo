package org.testadirapa.sesterzo.api.impl

import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.parameter
import io.ktor.http.ContentType.Application
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom
import io.ktor.util.date.GMTDate
import org.testadirapa.sesterzo.api.AttachmentApi
import org.testadirapa.sesterzo.api.CachedApi
import org.testadirapa.sesterzo.api.TemporizedCachedApi
import org.testadirapa.sesterzo.cache.AttachmentPersistentCache
import org.testadirapa.sesterzo.cache.model.CachedAttachment
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.http.wrap
import org.testadirapa.sesterzo.model.DecryptedAttachment
import org.testadirapa.sesterzo.model.EncryptedAttachment
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.services.CryptoService
import kotlin.time.Duration

class AttachmentApiImpl(
	httpConfig: HttpConfig,
	cache: AttachmentPersistentCache,
	ttl: Duration,
	private val authService: AuthService,
	private val cryptoService: CryptoService,
) : TemporizedCachedApi<EncryptedAttachment, CachedAttachment, AttachmentPersistentCache>(httpConfig, cache, ttl), AttachmentApi {

	override val baseSegment: String = "attachment"

	private suspend fun retrieveInSpace(
		spaceId: String,
		attachmentId: String
	): HttpResponse<EncryptedAttachment> = get {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "inSpace", spaceId, attachmentId)
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrap()

	override suspend fun getAttachmentInSpace(
		spaceId: String,
		attachmentId: String,
		bypassCache: Boolean): DecryptedAttachment? = cachedOrGetIfPresent(
			id = attachmentId,
			bypassCache = bypassCache
		) { id -> retrieveInSpace(spaceId, attachmentId = id) }?.let {
			cryptoService.decrypt(it)
	}
}