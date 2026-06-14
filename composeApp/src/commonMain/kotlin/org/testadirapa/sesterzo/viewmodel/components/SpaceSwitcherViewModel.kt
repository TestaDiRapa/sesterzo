package org.testadirapa.sesterzo.viewmodel.components

import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.model.Timestamp
import org.testadirapa.sesterzo.viewmodel.AbstractViewModel
import org.testadirapa.sesterzo.viewmodel.intents.Intent
import kotlin.concurrent.Volatile
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

class SpaceSwitcherViewModel(
	private val errorHandler: (Throwable) -> Unit,
) : AbstractViewModel<SpaceSwitcherViewModel.SpaceSwitcherIntent>() {
	override val logger = Logger.withTag("AppViewModel")

	private val _spacesState = MutableStateFlow<List<Space>>(emptyList())
	val spacesState: StateFlow<List<Space>> = _spacesState

	private val _spaceThumbnailsState = MutableStateFlow<Map<String, Base64String>>(emptyMap())
	val spaceThumbnailsState: StateFlow<Map<String, Base64String>> = _spaceThumbnailsState

	@Volatile
	private var lastUpdateTimestamp: Timestamp? = null

	sealed interface SpaceSwitcherIntent : Intent {
		data object RefreshSpaces: SpaceSwitcherIntent
		data object ResetSpaces: SpaceSwitcherIntent
	}

	override suspend fun processIntent(intent: SpaceSwitcherIntent) {
		when(intent) {
			is SpaceSwitcherIntent.RefreshSpaces -> {
				val now = Clock.System.now().toEpochMilliseconds()
				if (lastUpdateTimestamp == null || (now - lastUpdateTimestamp!!) >= 60.seconds.inWholeMilliseconds) {
					val spaces = AppCtx.api.space.getSpaces()
					_spacesState.value = spaces
					_spaceThumbnailsState.value = spaces.mapNotNull {
						it.pictureReference?.let { ref ->
							AppCtx.api.attachment.getAttachmentInSpace(
								spaceId = it.id,
								attachmentId = ref,
								bypassCache = false
							)?.let { attachment ->
								it.id to attachment.data
							}
						}
					}.toMap()
					lastUpdateTimestamp = now
				}
			}
			is SpaceSwitcherIntent.ResetSpaces -> {
				lastUpdateTimestamp = null
			}
		}
	}

	override fun onError(error: Throwable) {
		errorHandler(error)
	}

}