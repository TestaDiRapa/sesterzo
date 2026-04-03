package org.testadirapa.sesterzo.components.errors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.testadirapa.sesterzo.viewmodel.errors.ErrorState

@Composable
fun ErrorAlert(
	error: ErrorState?,
	onDismiss: () -> Unit,
) {
	AnimatedVisibility(
		visible = error != null,
		enter = slideInVertically { -it },
		exit = slideOutVertically { -it },
		modifier = Modifier.zIndex(1f)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.background(MaterialTheme.colorScheme.errorContainer)
				.padding(16.dp)
		) {
			Text(
				text = error?.message ?: "Unknown error",
				color = MaterialTheme.colorScheme.onErrorContainer
			)
		}
	}
}