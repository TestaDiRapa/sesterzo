package org.testadirapa.sesterzo.components.template

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.text.TextWithIcon
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.cycle
import sesterzo.composeapp.generated.resources.template_page_subtitle
import sesterzo.composeapp.generated.resources.template_page_title

@Composable
fun TemplateTitle() {
	Column {
		TextWithIcon(
			icon = painterResource(Res.drawable.cycle),
			text = stringResource(Res.string.template_page_subtitle),
			color = colorScheme.primary,
		)
		Text(
			text = stringResource(Res.string.template_page_title),
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.Bold,
			color = colorScheme.onBackground,
		)
	}
}