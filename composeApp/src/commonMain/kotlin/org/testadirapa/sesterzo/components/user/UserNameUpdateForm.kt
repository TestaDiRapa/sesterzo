package org.testadirapa.sesterzo.components.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.input.FormButton
import org.testadirapa.sesterzo.components.input.TextField
import org.testadirapa.sesterzo.model.User
import org.testadirapa.sesterzo.models.FormValue
import org.testadirapa.sesterzo.models.Optional
import org.testadirapa.sesterzo.validators.defaultNameValidator
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.register_name
import sesterzo.composeapp.generated.resources.register_name_error
import sesterzo.composeapp.generated.resources.register_name_placeholder

@Composable
fun UserNameUpdateForm(
	user: User,
	buttonLabel: String,
	isLoading: Boolean,
	onSubmit: (name: String) -> Unit,
) {
	var userName by remember {
		mutableStateOf(
			FormValue(
				value = Optional.Present(user.name),
				validator = defaultNameValidator
			)
		)
	}
	Column(
		verticalArrangement = Arrangement.spacedBy(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		TextField(
			value = userName,
			onValueChange = {
				userName = userName.update(it)
			},
			title = stringResource(Res.string.register_name),
			placeholder = stringResource(Res.string.register_name_placeholder),
			errorMessage = stringResource(Res.string.register_name_error),
		)
		FormButton(
			text = buttonLabel,
			enabled = userName.isValid,
			isLoading = isLoading,
			onClick = {
				onSubmit(userName.validValue)
			}
		)
	}
}