package org.testadirapa.sesterzo.screens.backup

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import com.icure.kryptom.utils.base32Encode
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.components.input.FormButton
import org.testadirapa.sesterzo.components.input.LabeledSwitch
import org.testadirapa.sesterzo.components.input.ReadOnlyCopyField
import org.testadirapa.sesterzo.components.text.MultilineBodyText
import org.testadirapa.sesterzo.components.ui.OrDivider
import org.testadirapa.sesterzo.model.Base32String
import org.testadirapa.sesterzo.model.Base64String
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.backup_key_confirm_button
import sesterzo.composeapp.generated.resources.backup_key_confirm_text
import sesterzo.composeapp.generated.resources.backup_key_description_1
import sesterzo.composeapp.generated.resources.backup_key_description_2
import sesterzo.composeapp.generated.resources.backup_key_description_3
import sesterzo.composeapp.generated.resources.backup_key_generate_recovery
import sesterzo.composeapp.generated.resources.backup_key_private_key
import sesterzo.composeapp.generated.resources.backup_key_recovery_description_1
import sesterzo.composeapp.generated.resources.backup_key_recovery_description_2
import sesterzo.composeapp.generated.resources.backup_key_recovery_title
import sesterzo.composeapp.generated.resources.backup_key_title

@Composable
fun BackupPrivateKeyScreen(
	onUserAccept: () -> Unit,
) {
	val scope = rememberCoroutineScope()
	var privateKey by remember { mutableStateOf<Base64String?>(null) }
	var recoveryKey by remember { mutableStateOf<Base32String?>(null) }
	var userAccepted by remember { mutableStateOf(false) }

	LaunchedEffect("privateKey") {
		privateKey = AppCtx.api.cryptoService.exportAndEncodePrivateKey()
	}

	Column {
		Text(
			text = stringResource(Res.string.backup_key_title),
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.Bold,
			color = colorScheme.onBackground,
		)
		MultilineBodyText(
			resources = listOf(
				Res.string.backup_key_description_1,
				Res.string.backup_key_description_2,
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
			)
		)
		recoveryKey?.also { key ->
			ReadOnlyCopyField(
				value = key,
				label = "RecoveryKey Key",
				title = stringResource(Res.string.backup_key_recovery_title),
			)
		} ?: FormButton(
			onClick = {
				scope.launch {
					val key = AppCtx.api.recoveryApi.generateRecoveryKey(
						owner = AppCtx.api.user.getCurrentUser().bodyOrThrow().id,
						expiresAt = null
					)
					recoveryKey = base32Encode(key)
				}
			},
			enabled = true,
			text = stringResource(Res.string.backup_key_generate_recovery)
		)

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