package org.testadirapa.sesterzo.components.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.components.input.Bip39RecoveryField
import org.testadirapa.sesterzo.components.input.ReadOnlyCopyField
import org.testadirapa.sesterzo.components.loading.PulsatingRoundedSquare
import org.testadirapa.sesterzo.components.qr.SesterzoQr
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Bip39RecoveryKey
import org.testadirapa.sesterzo.serialization.Serialization
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.backup_key_private_key
import sesterzo.composeapp.generated.resources.backup_key_recovery_title
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes

@Composable
fun KeyOverlay(
	contentType: OverlayContentType,
	onDismiss: () -> Unit,
	onError: (Throwable) -> Unit,
) {
	var isLoading by remember { mutableStateOf(true) }
	var privateKey by remember { mutableStateOf<Base64String?>(null) }
	var recoveryKeyAsListOfInt by remember { mutableStateOf<List<Int>?>(null) }
	var recoveryKey by remember { mutableStateOf<Bip39RecoveryKey?>(null) }

	LaunchedEffect(contentType) {
		runCatching {
			isLoading = true
			when (contentType) {
				OverlayContentType.PrivateKey -> {
					privateKey = AppCtx.api.cryptoService.exportAndEncodePrivateKey()
				}
				OverlayContentType.QR -> {
					recoveryKeyAsListOfInt = AppCtx.api.recovery.generateRecoveryKeyAndReturnBipIndexes(
						receiver = null,
						expiresAt = Clock.System.now().toEpochMilliseconds() + 5.minutes.inWholeMilliseconds,
					)
				}
				OverlayContentType.RecoveryKey -> {
					recoveryKey = AppCtx.api.recovery.generateRecoveryKey(
						receiver = null,
						expiresAt = null,
					)
				}
			}
			isLoading = false
		}.onFailure(onError)

	}

	Dialog(onDismissRequest = onDismiss) {
		Card(
			modifier = Modifier
				.padding(vertical = 32.dp)
				.fillMaxWidth(),
			border = BorderStroke(width = 1.dp, color = colorScheme.outline),
			colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
		) {
			Row(
				modifier = Modifier.padding(16.dp).fillMaxWidth(),
				horizontalArrangement = Arrangement.Center,
			) {
				when {
					isLoading -> PulsatingRoundedSquare(
						index = 1,
						total = 1,
						height = 192.dp,
						width = 192.dp,
					)
					contentType == OverlayContentType.QR -> {
						recoveryKeyAsListOfInt?.let { key ->
							SesterzoQr(data = Serialization.json.encodeToString(key), description = "Recovery QR")
						}
					}
					contentType == OverlayContentType.RecoveryKey -> {
						recoveryKey?.let { key ->
							Bip39RecoveryField(
								words = key.words,
								label = "Recovery Key",
								title = stringResource(Res.string.backup_key_recovery_title),
							)
						}
					}
					contentType == OverlayContentType.PrivateKey -> {
						privateKey?.let { key ->
							ReadOnlyCopyField(
								value = key,
								label = "Private Key",
								title = stringResource(Res.string.backup_key_private_key),
							)
						}
					}
				}
			}
		}
	}
}