package org.testadirapa.sesterzo.components.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.app_name
import sesterzo.composeapp.generated.resources.sesterzo

@Composable
fun LogoWithName() {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(4.dp),
	) {
		Image(
			painter = painterResource(Res.drawable.sesterzo),
			contentDescription = null,
			modifier = Modifier.size(48.dp)
		)
		Text(
			text = stringResource(Res.string.app_name),
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.Bold,
			color = colorScheme.onBackground,
		)
	}
}