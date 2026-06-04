package org.testadirapa.sesterzo.api.impl

import com.icure.kryptom.crypto.defaultCryptoService
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
import org.testadirapa.sesterzo.api.SpaceApi
import org.testadirapa.sesterzo.api.TemporizedCachedApi
import org.testadirapa.sesterzo.cache.SpacePersistentCache
import org.testadirapa.sesterzo.cache.model.CachedSpace
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.http.wrap
import org.testadirapa.sesterzo.model.DecryptedAttachment
import org.testadirapa.sesterzo.model.EncryptedAttachment
import org.testadirapa.sesterzo.model.RGBColor
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.model.SpaceStub
import org.testadirapa.sesterzo.model.UserAccessKey
import org.testadirapa.sesterzo.model.UserSpaceRole
import org.testadirapa.sesterzo.model.dto.SpaceUpdatePayload
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.services.CryptoService
import kotlin.time.Duration

class SpaceApiImpl(
	httpConfig: HttpConfig,
	cache: SpacePersistentCache,
	ttl: Duration,
	private val authService: AuthService,
	private val cryptoService: CryptoService,
) : TemporizedCachedApi<Space, CachedSpace, SpacePersistentCache>(httpConfig, cache, ttl), SpaceApi {

	override val baseSegment: String = "space"

	private suspend fun retrieveSpaces(): HttpResponse<List<Space>> = get {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment)
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrap()

	private suspend fun retrieveSpace(spaceId: String): HttpResponse<Space> = get {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, spaceId)
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrap()

	private suspend fun createSpaceFromStub(stub: SpaceStub): HttpResponse<Space> = post {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
		contentType(Application.Json)
		setBody(stub)
	}.wrap()

	private suspend fun setSpaceThumbnail(spaceId: String, picture: EncryptedAttachment): HttpResponse<Space> = put {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, spaceId, "thumbnail")
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
		contentType(Application.Json)
		setBody(picture)
	}.wrap()

	private suspend fun setSpaceNameAndColor(spaceId: String, name: String, color: RGBColor): HttpResponse<Space> = put {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, spaceId)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
		contentType(Application.Json)
		setBody(
			SpaceUpdatePayload(
				name = name,
				color = color,
			)
		)
	}.wrap()

	override suspend fun getSpaces(): List<Space> = getAllMerging {
		retrieveSpaces()
	}.onEach {
		cryptoService.decryptAndLoadSpaceKey(it)
	}

	override suspend fun getSpace(spaceId: String, bypassCache: Boolean): Space = cachedOrGet(
		id = spaceId,
		bypassCache = bypassCache,
	) { retrieveSpace(spaceId) }

	override suspend fun createSpace(
		name: String,
		picture: ByteArray?,
		fallbackColor: RGBColor?,
	): Space {
		val stub = SpaceStub(
			id = defaultCryptoService.strongRandom.randomUUID(),
			name = name,
			color = fallbackColor,
			users = mapOf(
				cryptoService.userId to UserAccessKey(
					accessLevel = UserSpaceRole.Admin,
					encryptedKey = cryptoService.generateAndEncryptSpaceKey()
				)
			)
		)
		val newSpace = createSpaceFromStub(stub).bodyOrThrow()
		authService.invalidateToken()
		putInCache(newSpace)
		cryptoService.decryptAndLoadSpaceKey(newSpace)
		return picture?.let {
			DecryptedAttachment(
				id = defaultCryptoService.strongRandom.randomUUID(),
				spaceId = newSpace.id,
				data = base64Encode(it),
			)
		}?.let {
			val attachment = cryptoService.encrypt(it)
			setSpaceThumbnail(
				spaceId = newSpace.id,
				picture = attachment
			).bodyOrThrow()
		}?.also {
			putInCache(it)
		} ?: newSpace
	}

	override suspend fun updateSpace(
		spaceId: String,
		name: String,
		picture: ByteArray?,
		color: RGBColor,
	): Space {
		val updatedSpace = setSpaceNameAndColor(
			spaceId = spaceId,
			name = name,
			color = color,
		).bodyOrThrow()
		putInCache(updatedSpace)
		return picture?.let {
			DecryptedAttachment(
				id = defaultCryptoService.strongRandom.randomUUID(),
				spaceId = spaceId,
				data = base64Encode(it),
			)
		}?.let {
			val attachment = cryptoService.encrypt(it)
			setSpaceThumbnail(
				spaceId = spaceId,
				picture = attachment
			).bodyOrThrow()
		}?.also {
			putInCache(it)
		} ?: updatedSpace
	}
}