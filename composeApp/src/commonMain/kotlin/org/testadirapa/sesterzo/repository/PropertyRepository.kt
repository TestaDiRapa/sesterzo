package org.testadirapa.sesterzo.repository

import org.testadirapa.sesterzo.storage.StorageFacade

class PropertyRepository(
	val datastore: StorageFacade
) {

	companion object {
		private const val REFRESH_JWT_KEY = "org.testadirapa.sesterzo.refreshJwt"
		private const val AUTH_JWT_KEY = "org.testadirapa.sesterzo.authJwt"
	}

	suspend fun clear() {
		datastore.removeItem(AUTH_JWT_KEY)
		datastore.removeItem(REFRESH_JWT_KEY)
	}

	suspend fun getJwt(): String? = datastore.getItem(AUTH_JWT_KEY)
	suspend fun setJwt(jwt: String) { datastore.setItem(AUTH_JWT_KEY, jwt) }

	suspend fun getRefreshJwt(): String? = datastore.getItem(REFRESH_JWT_KEY)
	suspend fun setRefreshJwt(jwt: String) { datastore.setItem(REFRESH_JWT_KEY, jwt) }

}