package org.testadirapa.sesterzo.screens.crypto

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.components.input.FormButton
import org.testadirapa.sesterzo.components.input.LabeledSwitch
import org.testadirapa.sesterzo.components.input.ReadOnlyCopyField
import org.testadirapa.sesterzo.components.text.MultilineBodyText
import org.testadirapa.sesterzo.components.ui.OrDivider
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Bip39RecoveryKey
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.backup_key_confirm_button
import sesterzo.composeapp.generated.resources.backup_key_confirm_text
import sesterzo.composeapp.generated.resources.backup_key_description_1
import sesterzo.composeapp.generated.resources.backup_key_description_2_1
import sesterzo.composeapp.generated.resources.backup_key_description_2_2
import sesterzo.composeapp.generated.resources.backup_key_description_2_3
import sesterzo.composeapp.generated.resources.backup_key_description_2_4
import sesterzo.composeapp.generated.resources.backup_key_description_3
import sesterzo.composeapp.generated.resources.backup_key_generate_recovery
import sesterzo.composeapp.generated.resources.backup_key_private_key
import sesterzo.composeapp.generated.resources.backup_key_recovery_description_1
import sesterzo.composeapp.generated.resources.backup_key_recovery_description_2
import sesterzo.composeapp.generated.resources.backup_key_recovery_description_3
import sesterzo.composeapp.generated.resources.backup_key_recovery_title
import sesterzo.composeapp.generated.resources.backup_key_title

@Composable
fun BackupPrivateKeyScreen(
	isMobile: Boolean,
	onUserAccept: () -> Unit,
	onError: (error: Throwable) -> Unit,
) {
	val scope = rememberCoroutineScope()
	var privateKey by remember { mutableStateOf<Base64String?>(null) }
	var recoveryKey by remember { mutableStateOf<Bip39RecoveryKey?>(null) }
	var userAccepted by remember { mutableStateOf(false) }

	LaunchedEffect("privateKey") {
		privateKey = AppCtx.api.cryptoService.exportAndEncodePrivateKey()
	}

	Scaffold { innerPadding ->
		Box(
			modifier = Modifier.fillMaxSize().padding(innerPadding),
			contentAlignment = Alignment.Center,
		) {
			val content: @Composable () -> Unit = {
				Column(
					modifier = Modifier
						.padding(horizontal = 24.dp)
						.padding(vertical = 24.dp),
					verticalArrangement = Arrangement.spacedBy(16.dp),
				) {
					Text(
						text = stringResource(Res.string.backup_key_title),
						style = MaterialTheme.typography.titleLarge,
						fontWeight = FontWeight.Bold,
						color = colorScheme.onBackground,
					)
					MultilineBodyText(
						resources = listOf(
							Res.string.backup_key_description_1,
							Res.string.backup_key_description_2_1,
							Res.string.backup_key_description_2_2,
							Res.string.backup_key_description_2_3,
							Res.string.backup_key_description_2_4,
							Res.string.backup_key_description_3
						)
					)
					privateKey?.also { key ->
						ReadOnlyCopyField(
							value = key,
							label = "Private Key",
							title = stringResource(Res.string.backup_key_private_key),
						)
					}
					OrDivider()

					Text(
						text = stringResource(Res.string.backup_key_recovery_title),
						style = MaterialTheme.typography.titleMedium,
						fontWeight = FontWeight.Bold,
						color = colorScheme.onBackground,
					)
					MultilineBodyText(
						resources = listOf(
							Res.string.backup_key_recovery_description_1,
							Res.string.backup_key_recovery_description_2,
							Res.string.backup_key_recovery_description_3,
						)
					)
					recoveryKey?.also { key ->
						ReadOnlyCopyField(
							value = key.words.joinToString(" "),
							label = "Recovery Key",
							title = stringResource(Res.string.backup_key_recovery_title),
						)
					} ?: FormButton(
						onClick = {
							scope.launch {
								runCatching {
									recoveryKey = AppCtx.api.recovery.generateRecoveryKey(
										owner = AppCtx.api.currentUserId,
										expiresAt = null
									)
								}.onFailure(onError)
							}
						},
						enabled = true,
						text = stringResource(Res.string.backup_key_generate_recovery)
					)

					HorizontalDivider(modifier = Modifier.weight(1f))

					LabeledSwitch(
						label = stringResource(Res.string.backup_key_confirm_text),
						initialValue = userAccepted,
						onCheckedChange = { userAccepted = it },
					)

					FormButton(
						onClick = onUserAccept,
						enabled = userAccepted,
						text = stringResource(Res.string.backup_key_confirm_button),
					)
				}
			}

			if (isMobile) {
				content()
			} else {
				Card(
					modifier = Modifier
						.widthIn(max = 1200.dp)
						.heightIn(max = 720.dp),
					border = BorderStroke(width = 2.dp, color = colorScheme.onBackground),
					colors = CardDefaults.cardColors(containerColor = colorScheme.background),
				) {
					content()
				}
			}
		}
	}
}
