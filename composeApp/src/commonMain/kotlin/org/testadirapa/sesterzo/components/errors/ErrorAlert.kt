package org.testadirapa.sesterzo.components.errors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.jetbrains.compose.resources.painterResource
import org.testadirapa.sesterzo.viewmodel.errors.ErrorState
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.cancel_icon

@Composable
fun ErrorAlert(
	error: ErrorState?,
	onDismiss: () -> Unit,
	modifier: Modifier = Modifier,
) {
	AnimatedVisibility(
		visible = error != null,
		enter = slideInVertically { -it },
		exit = slideOutVertically { -it },
		modifier = modifier.zIndex(1f)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.background(
					color = MaterialTheme.colorScheme.errorContainer,
					shape = MaterialTheme.shapes.medium
				)
				.padding(horizontal = 16.dp, vertical = 8.dp)
		) {
			IconButton(onClick = onDismiss) {
				Icon(
					painter = painterResource(Res.drawable.cancel_icon),
					contentDescription = "Dismiss",
					tint = MaterialTheme.colorScheme.onErrorContainer
				)
			}
			Text(
				text = error?.message ?: "Unknown error",
				color = MaterialTheme.colorScheme.onErrorContainer
			)
		}
	}
}