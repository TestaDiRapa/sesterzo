package org.testadirapa.sesterzo.api.impl

import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.ContentType.Application
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.util.date.GMTDate
import org.testadirapa.sesterzo.api.TemporizedCachedApi
import org.testadirapa.sesterzo.api.UserApi
import org.testadirapa.sesterzo.cache.UserPersistentCache
import org.testadirapa.sesterzo.cache.model.CachedUser
import org.testadirapa.sesterzo.config.HttpConfig
import org.testadirapa.sesterzo.http.HttpResponse
import org.testadirapa.sesterzo.http.wrap
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.User
import org.testadirapa.sesterzo.model.dto.PublicKeyPayload
import org.testadirapa.sesterzo.services.AuthService
import org.testadirapa.sesterzo.storage.StorageFacade
import kotlin.time.Duration

class UserApiImpl(
	httpConfig: HttpConfig,
	cache: UserPersistentCache,
	ttl: Duration,
	private val localStorage: StorageFacade,
	private val authService: AuthService,
) : TemporizedCachedApi<User, CachedUser, UserPersistentCache>(httpConfig, cache, ttl), UserApi {

	companion object {
		private const val CURRENT_USER_ID_KEY = "org.testadirapa.sesterzo.currentUserId"
	}

	override val baseSegment: String = "user"

	private suspend fun retrieveCurrentUser(): HttpResponse<User> = get {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "current")
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
	}.wrap()

	private suspend fun retrieveUsers(userIds: Set<String>): HttpResponse<List<User>> = post {
		url {
			takeFrom(baseUrl)
			appendPathSegments(baseSegment, "byIds")
			parameter("ts", GMTDate().timestamp)
		}
		bearerAuth(authService.getJwt())
		accept(Application.Json)
		contentType(Application.Json)
		setBody(userIds)
	}.wrap()

	private suspend fun updatePublicKeyForCurrentUser(publicKey: Base64String): HttpResponse<User> =
		post {
			url {
				takeFrom(baseUrl)
				appendPathSegments(baseSegment, "current", "publicKey")
			}
			bearerAuth(authService.getJwt())
			accept(Application.Json)
			contentType(Application.Json)
			setBody(PublicKeyPayload(publicKey))
		}.wrap()

	private suspend fun updateBackupConfirmation(): HttpResponse<User> =
		put {
			url {
				takeFrom(baseUrl)
				appendPathSegments(baseSegment, "current", "hasBackup")
			}
			bearerAuth(authService.getJwt())
			accept(Application.Json)
		}.wrap()

	override suspend fun getCurrentUser(): User {
		val currentUserId = localStorage.getItem(CURRENT_USER_ID_KEY)
		return if (currentUserId != null) {
			cachedOrGet(currentUserId, bypassCache = false) { retrieveCurrentUser() }
		} else {
			val currentUser = retrieveCurrentUser().bodyOrThrow()
			localStorage.setItem(CURRENT_USER_ID_KEY, currentUser.id)
			putInCache(currentUser)
			currentUser
		}
	}

	override suspend fun setPublicKeyForCurrentUser(publicKey: Base64String): User =
		updatePublicKeyForCurrentUser(publicKey).bodyOrThrow().also {
			putInCache(it)
		}

	override suspend fun setBackupConfirmation(): User =
		updateBackupConfirmation().bodyOrThrow().also {
			putInCache(it)
		}

	override suspend fun getUsers(userIds: List<String>, bypassCache: Boolean): List<User> = cachedAndGetMissing(
		ids = userIds,
		bypassCache = bypassCache
	) { ids -> retrieveUsers(userIds = ids.toSet()) }
}