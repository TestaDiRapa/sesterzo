package org.testadirapa.sesterzo.repository

import org.testadirapa.sesterzo.storage.StorageFacade

class PropertyRepository(
	val datastore: StorageFacade
) {

	companion object {
		private const val REFRESH_JWT_KEY = "org.testadirapa.sesterzo.refreshJwt"
		private const val AUTH_JWT_KEY = "org.testadirapa.sesterzo.authJwt"
		private const val DEFAULT_SPACE = "org.testadirapa.sesterzo.defaultSpace"
	}

	suspend fun clear() {
		datastore.removeItem(AUTH_JWT_KEY)
		datastore.removeItem(REFRESH_JWT_KEY)
	}

	suspend fun getJwt(): String? = datastore.getItem(AUTH_JWT_KEY)
	suspend fun setJwt(jwt: String) { datastore.setItem(AUTH_JWT_KEY, jwt) }
	suspend fun resetJwt() { datastore.removeItem(AUTH_JWT_KEY) }

	suspend fun getRefreshJwt(): String? = datastore.getItem(REFRESH_JWT_KEY)
	suspend fun setRefreshJwt(jwt: String) { datastore.setItem(REFRESH_JWT_KEY, jwt) }
	suspend fun resetRefreshJwt() { datastore.removeItem(REFRESH_JWT_KEY) }

	suspend fun getDefaultSpace(): String? = datastore.getItem(DEFAULT_SPACE)
	suspend fun setDefaultSpace(spaceId: String) { datastore.setItem(DEFAULT_SPACE, spaceId) }

}