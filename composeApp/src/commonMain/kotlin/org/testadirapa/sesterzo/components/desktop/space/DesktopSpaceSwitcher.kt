package org.testadirapa.sesterzo.components.desktop.space

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import org.testadirapa.sesterzo.components.space.SpaceAvatar
import org.testadirapa.sesterzo.model.Base64String
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.viewmodel.components.SpaceSwitcherViewModel
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.arrow_down

@Composable
fun DesktopSpaceSwitcher(
	space: Space,
	spaceThumbnail: Base64String?,
	onSelect: (Space) -> Unit,
	onCreate: () -> Unit,
	onJoin: () -> Unit,
	onError: (Throwable) -> Unit
) {
	val viewModel = viewModel { SpaceSwitcherViewModel(onError) }
	val isLoading = viewModel.loadingState.collectAsState()

	Card(
		border = BorderStroke(width = 1.dp, color = colorScheme.outline),
		colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant),
	)  {
		Row(
			modifier = Modifier.fillMaxWidth().padding(8.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(16.dp)
			) {
				SpaceAvatar(
					space = space,
					thumbnail = spaceThumbnail,
					size = 36.dp
				)
				Text(
					text = space.name,
					fontSize = 14.5.sp,
					fontWeight = FontWeight.SemiBold,
					color = colorScheme.onSurface,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
				)
			}
			Icon(
				modifier = Modifier.height(18.dp).width(18.dp),
				painter = painterResource(Res.drawable.arrow_down),
				contentDescription = "Show Space menu",
				tint = colorScheme.onSurfaceVariant,
			)
		}
	}
}