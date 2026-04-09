package org.testadirapa.sesterzo.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.input.EmailField
import org.testadirapa.sesterzo.components.input.FormButton
import org.testadirapa.sesterzo.components.input.ValidationCodeField
import org.testadirapa.sesterzo.components.text.TitleAndSubtitle
import org.testadirapa.sesterzo.components.ui.LogoWithName
import org.testadirapa.sesterzo.handlers.MutableStateFlowCaptchaProgressHandler
import org.testadirapa.sesterzo.models.FormValue
import org.testadirapa.sesterzo.screens.SwitchAuthButton
import org.testadirapa.sesterzo.validators.EmailValidator
import org.testadirapa.sesterzo.validators.OttValidator
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.auth_code_label
import sesterzo.composeapp.generated.resources.button_register
import sesterzo.composeapp.generated.resources.login_button
import sesterzo.composeapp.generated.resources.login_complete_button
import sesterzo.composeapp.generated.resources.login_subtitle
import sesterzo.composeapp.generated.resources.login_title

@Composable
fun LoginScreen(
	isLoading: Boolean,
	onStartLogin: (email: String) -> Unit,
	onCompleteLogin: (ott: String) -> Unit,
	switchToRegister: () -> Unit,
	captchaProgressState: StateFlow<MutableStateFlowCaptchaProgressHandler.CaptchaProgress>
) {
	var email by remember { mutableStateOf(FormValue(validator = EmailValidator)) }
	var ott by remember { mutableStateOf(FormValue(validator = OttValidator)) }
	val captchaProgress by captchaProgressState.collectAsState()
	Scaffold { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.padding(horizontal = 24.dp)
				.padding(top = 48.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp),
		) {
			LogoWithName()
			TitleAndSubtitle(
				title = stringResource(Res.string.login_title),
				subtitle = stringResource(Res.string.login_subtitle),
			)
			EmailField(
				value = email,
				enabled = captchaProgress.isUninitialised,
				onValueChange = {
					email = email.update(it)
				},
			)
			captchaProgress.loadingValue?.also { progress ->
				LinearProgressIndicator(
					progress = { progress.toFloat()},
					modifier = Modifier.fillMaxWidth(),
				)
			}
			captchaProgress.takeIf { !it.isComplete }?.also { captcha ->
				FormButton(
					onClick = {
						onStartLogin(email.validValue)
					},
					isLoading = isLoading,
					enabled = captcha.isUninitialised && email.isValid,
					text = stringResource(Res.string.login_button)
				)
			}
			captchaProgress.takeIf { it.isComplete }?.also {
				Text(
					text = stringResource(Res.string.auth_code_label),
					style = MaterialTheme.typography.titleMedium,
					color = colorScheme.onSurfaceVariant,
				)
				ValidationCodeField(
					value = ott,
					onValueChange = {
						ott = ott.update(it)
					}
				)
				FormButton(
					onClick = {
						onCompleteLogin(ott.validValue)
					},
					isLoading = isLoading,
					enabled = ott.isValid,
					text = stringResource(Res.string.login_complete_button)
				)
			}
			SwitchAuthButton(
				onClick = switchToRegister,
				text = stringResource(Res.string.button_register)
			)
		}
	}
}