package org.testadirapa.sesterzo.viewmodel.components

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.icure.kryptom.utils.base64Encode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Currency
import org.testadirapa.sesterzo.model.RGBColor
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.model.User
import org.testadirapa.sesterzo.viewmodel.AbstractViewModel
import org.testadirapa.sesterzo.viewmodel.intents.Intent

class SettingsPageViewModel(
	private val errorHandler: (Throwable) -> Unit,
) : AbstractViewModel<SettingsPageViewModel.SettingsIntents>() {

	override val logger = Logger.withTag("SpaceSwitcherViewModel")

	private val _currentUserState = MutableStateFlow<User?>(null)
	val currentUserState: StateFlow<User?> = _currentUserState

	sealed interface SettingsIntents : Intent {
		data class SetCurrency(val currency: Currency) : SettingsIntents
		data class SetUserName(val name: String) : SettingsIntents
		class UpdateSpace(
			val spaceId: String,
			val name: String,
			val image: ByteArray?,
			val color: RGBColor,
			val onSpaceUpdate: (space: Space, thumbnail: Base64String?) -> Unit,
		) : SettingsIntents
		data class ErrorsOptIn(val optIn: Boolean) : SettingsIntents
	}

	init {
		viewModelScope.launch {
			_currentUserState.update {
				AppCtx.api.user.getCurrentUser()
			}
		}
	}

	override suspend fun processIntent(intent: SettingsIntents) {
		when (intent) {
			is SettingsIntents.SetCurrency -> {
				val updatedUser = AppCtx.api.user.setCurrency(currency = intent.currency)
				AppCtx.currency = intent.currency
				_currentUserState.update { updatedUser }
			}
			is SettingsIntents.SetUserName -> {
				_currentUserState.update {
					AppCtx.api.user.setName(name = intent.name)
				}
			}
			is SettingsIntents.UpdateSpace -> {
				val result = AppCtx.api.space.updateSpace(
					spaceId = intent.spaceId,
					name = intent.name,
					picture = intent.image,
					color = intent.color
				)
				intent.onSpaceUpdate(result, intent.image?.let { base64Encode(it) })
			}
			is SettingsIntents.ErrorsOptIn -> {
				_currentUserState.update {
					AppCtx.api.user.setLogsOptIn(optIn = intent.optIn)
				}
			}
		}
	}

	override fun onError(error: Throwable) {
		errorHandler(error)
	}

}