package org.testadirapa.sesterzo.pages.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.input.EmailField
import org.testadirapa.sesterzo.components.input.TextField
import org.testadirapa.sesterzo.components.text.TitleAndSubtitle
import org.testadirapa.sesterzo.models.FormValue
import org.testadirapa.sesterzo.validators.EmailValidator
import org.testadirapa.sesterzo.validators.NotBlankValidator
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.register_name
import sesterzo.composeapp.generated.resources.register_name_error
import sesterzo.composeapp.generated.resources.register_name_placeholder
import sesterzo.composeapp.generated.resources.register_subtitle
import sesterzo.composeapp.generated.resources.register_title

@Composable
fun RegistrationPage() {
	var email by remember { mutableStateOf(FormValue(validator = EmailValidator)) }
	var name by remember { mutableStateOf(FormValue(validator = NotBlankValidator)) }
	Scaffold { innerPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
				.padding(horizontal = 24.dp)
				.padding(top = 48.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp),

		) {
			TitleAndSubtitle(
				title = stringResource(Res.string.register_title),
				subtitle = stringResource(Res.string.register_subtitle),
			)
			EmailField(
				value = email,
				onValueChange = {
					email = email.update(it)
				},
			)
			TextField(
				value = name,
				title = stringResource(Res.string.register_name),
				placeholder = stringResource(Res.string.register_name_placeholder),
				errorMessage = stringResource(Res.string.register_name_error),
				onValueChange = {
					name = name.update(it)
				},
			)
		}
	}

}