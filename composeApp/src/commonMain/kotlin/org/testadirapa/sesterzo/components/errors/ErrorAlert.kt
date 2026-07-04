package org.testadirapa.sesterzo.components.errors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.testadirapa.sesterzo.viewmodel.errors.ErrorState
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.cancel_icon
import kotlin.time.Duration.Companion.milliseconds

private const val AUTO_DISMISS_MILLIS = 5000

@Composable
fun ErrorAlert(
	error: ErrorState?,
	onDismiss: () -> Unit,
	modifier: Modifier = Modifier,
) {
	// Restart the countdown whenever a new error is shown.
	var running by remember(error) { mutableStateOf(error != null) }
	val progress by animateFloatAsState(
		targetValue = if (running) 0f else 1f,
		animationSpec = tween(
			durationMillis = if (running) AUTO_DISMISS_MILLIS else 0,
			easing = LinearEasing
		),
		label = "errorAutoDismissProgress"
	)

	LaunchedEffect(error) {
		if (error != null) {
			running = true
			delay(AUTO_DISMISS_MILLIS.milliseconds)
			onDismiss()
		}
	}

	AnimatedVisibility(
		visible = error != null,
		enter = slideInVertically { -it },
		exit = slideOutVertically { -it },
		modifier = modifier.zIndex(1f)
	) {
		Column(
			modifier = Modifier
				.padding(horizontal = 16.dp)
				.fillMaxSize()
		) {
			Spacer(modifier = Modifier.fillMaxWidth().height(32.dp))
			Column(
				modifier = Modifier
					.clip(MaterialTheme.shapes.medium)
					.background(color = MaterialTheme.colorScheme.errorContainer)
					.fillMaxWidth()
			) {
				Row(
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier
						.fillMaxWidth()
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
				LinearProgressIndicator(
					progress = { progress },
					modifier = Modifier
						.fillMaxWidth()
						.height(4.dp),
					color = MaterialTheme.colorScheme.onErrorContainer,
					trackColor = MaterialTheme.colorScheme.errorContainer,
				)
			}
		}
	}
}
