package org.testadirapa.sesterzo.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.ui.OrDivider
import org.testadirapa.sesterzo.handlers.MutableStateFlowCaptchaProgressHandler
import org.testadirapa.sesterzo.screens.login.LoginScreen
import org.testadirapa.sesterzo.screens.register.RegistrationScreen
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.button_register

enum class AuthVariant { Login, Register}

@Composable
fun AuthScreen(
	onStartRegistration: (email: String, name: String) -> Unit,
	onStartLogin: (email: String) -> Unit,
	onCompleteAuth: (ott: String) -> Unit,
	captchaProgressState: StateFlow<MutableStateFlowCaptchaProgressHandler.CaptchaProgress>
) {
	var variant by remember { mutableStateOf(AuthVariant.Login) }
	when (variant) {
		AuthVariant.Login -> LoginScreen(
			onStartLogin = onStartLogin,
			onCompleteLogin = onCompleteAuth,
			captchaProgressState = captchaProgressState,
			switchToRegister = { variant = AuthVariant.Register }
		)
		AuthVariant.Register -> RegistrationScreen(
			onStartRegistration = onStartRegistration,
			onCompleteRegistration = onCompleteAuth,
			captchaProgressState = captchaProgressState,
			switchToLogin = { variant = AuthVariant.Login }
		)
	}
}

@Composable
fun SwitchAuthButton(
	onClick: () -> Unit,
	text: String,
) {
	OrDivider()
	Button(
		onClick = onClick,
		modifier = Modifier.fillMaxWidth().height(48.dp),
		shape = RoundedCornerShape(8.dp),
		border = BorderStroke(width = 2.dp, color = colorScheme.onBackground),
		colors = ButtonColors(
			containerColor = colorScheme.background,
			contentColor = colorScheme.onBackground,
			disabledContainerColor = colorScheme.background,
			disabledContentColor = colorScheme.onBackground,
		)
	) {
		Text(text)
	}
}