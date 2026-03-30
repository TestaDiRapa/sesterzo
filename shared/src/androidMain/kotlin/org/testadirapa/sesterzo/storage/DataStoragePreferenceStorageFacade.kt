package org.testadirapa.sesterzo.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStorePreferenceStorage(
	private val dataStore: DataStore<Preferences>
): StorageFacade {
	override suspend fun getItem(key: String): String? {
		return dataStore.data.map { prefs ->
			prefs[key.toPreferencesKey()]
		}.first()
	}

	override suspend fun setItem(key: String, value: String) {
		dataStore.edit { prefs ->
			prefs[key.toPreferencesKey()] = value
		}
	}

	override suspend fun removeItem(key: String) {
		dataStore.edit { prefs ->
			prefs.remove(key.toPreferencesKey())
		}
	}

	private fun String.toPreferencesKey() = stringPreferencesKey(this)
}
