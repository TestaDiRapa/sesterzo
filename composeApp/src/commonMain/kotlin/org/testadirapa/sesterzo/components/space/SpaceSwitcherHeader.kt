package org.testadirapa.sesterzo.components.space

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.BuildKonfig
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.out_of
import sesterzo.composeapp.generated.resources.swich_space_label
import sesterzo.composeapp.generated.resources.your_spaces

@Composable
fun SpaceSwitcherHeader(
	count: Int,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		verticalAlignment = Alignment.Bottom,
	) {
		Column(Modifier.weight(1f)) {
			Text(
				text = stringResource(Res.string.swich_space_label),
				style = MaterialTheme.typography.labelSmall,
				color = colorScheme.onSurfaceVariant,
			)
			Spacer(Modifier.height(4.dp))
			Text(
				text = stringResource(Res.string.your_spaces),
				fontSize = 17.sp,
				fontWeight = FontWeight.SemiBold,
				color = colorScheme.onSurface,
			)
		}
		Text(
			text = "$count ${stringResource(Res.string.out_of)} ${BuildKonfig.spaceLimit}",
			style = MaterialTheme.typography.labelMedium,
			color = colorScheme.onSurfaceVariant,
		)
	}
}