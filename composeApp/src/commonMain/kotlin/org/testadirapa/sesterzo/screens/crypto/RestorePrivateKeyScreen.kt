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
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.input.FormButton
import org.testadirapa.sesterzo.components.input.TextField
import org.testadirapa.sesterzo.components.text.TitleAndSubtitle
import org.testadirapa.sesterzo.components.ui.OrDivider
import org.testadirapa.sesterzo.model.Base32String
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.models.FormValue
import org.testadirapa.sesterzo.validators.Base32Validator
import org.testadirapa.sesterzo.validators.Base64Validator
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.recover_key_button_confirm
import sesterzo.composeapp.generated.resources.recover_key_description_1
import sesterzo.composeapp.generated.resources.recover_key_invalid_key_format
import sesterzo.composeapp.generated.resources.recover_key_option_recovery_key
import sesterzo.composeapp.generated.resources.recover_key_option_restore_key
import sesterzo.composeapp.generated.resources.recover_key_private_key_placeholder
import sesterzo.composeapp.generated.resources.recover_key_recovery_key_placeholder
import sesterzo.composeapp.generated.resources.recover_key_title

private enum class RecoveryOption { PrivateKey, RecoveryKey }

@Composable
fun RestorePrivateKeyScreen(
	isMobile: Boolean,
	onRestoreWithPrivateKey: (Base64String) -> Unit,
	onRestoreWithRecoveryKey: (Base32String) -> Unit,
) {
	var privateKeyBase64 by remember { mutableStateOf(FormValue(validator = Base64Validator)) }
	var recoveryKeyBase32 by remember { mutableStateOf(FormValue(validator = Base32Validator)) }
	var recoveryOption by remember { mutableStateOf<RecoveryOption?>(null) }
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
					TitleAndSubtitle(
						title = stringResource(Res.string.recover_key_title),
						subtitle = stringResource(Res.string.recover_key_description_1),
					)
					when (recoveryOption) {
						RecoveryOption.PrivateKey -> {
							TextField(
								value = privateKeyBase64,
								title = stringResource(Res.string.recover_key_option_restore_key),
								placeholder = stringResource(Res.string.recover_key_private_key_placeholder),
								errorMessage = stringResource(Res.string.recover_key_invalid_key_format),
								onValueChange = {
									privateKeyBase64 = privateKeyBase64.update(it)
								},
							)
							FormButton(
								onClick = { onRestoreWithPrivateKey(privateKeyBase64.validValue) },
								enabled = privateKeyBase64.isValid,
								text = stringResource(Res.string.recover_key_button_confirm),
							)
							OrDivider()
							FormButton(
								onClick = { recoveryOption = RecoveryOption.RecoveryKey },
								enabled = true,
								text = stringResource(Res.string.recover_key_option_recovery_key),
							)
						}
						RecoveryOption.RecoveryKey -> {
							TextField(
								value = recoveryKeyBase32,
								title = stringResource(Res.string.recover_key_option_recovery_key),
								placeholder = stringResource(Res.string.recover_key_recovery_key_placeholder),
								errorMessage = stringResource(Res.string.recover_key_invalid_key_format),
								onValueChange = {
									recoveryKeyBase32 = recoveryKeyBase32.update(it)
								},
							)
							FormButton(
								onClick = { onRestoreWithRecoveryKey(recoveryKeyBase32.validValue) },
								enabled = recoveryKeyBase32.isValid,
								text = stringResource(Res.string.recover_key_button_confirm),
							)
							OrDivider()
							FormButton(
								onClick = { recoveryOption = RecoveryOption.PrivateKey },
								enabled = true,
								text = stringResource(Res.string.recover_key_option_restore_key),
							)
						}
						null -> {
							FormButton(
								onClick = { recoveryOption = RecoveryOption.PrivateKey },
								enabled = true,
								text = stringResource(Res.string.recover_key_option_restore_key),
							)
							OrDivider()
							FormButton(
								onClick = { recoveryOption = RecoveryOption.RecoveryKey },
								enabled = true,
								text = stringResource(Res.string.recover_key_option_recovery_key),
							)
						}
					}
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