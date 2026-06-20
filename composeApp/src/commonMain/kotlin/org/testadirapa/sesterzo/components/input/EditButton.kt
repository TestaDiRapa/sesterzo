package org.testadirapa.sesterzo.components.input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.main_page_edit_block
import sesterzo.composeapp.generated.resources.pencil

@Composable
fun EditButton(
	onEdit: () -> Unit,
) {
	Button(
		onClick = onEdit,
		enabled = true,
		modifier = Modifier
			.height(36.dp)
			.defaultMinSize(minWidth = 42.dp),
		shape = RoundedCornerShape(8.dp),
		border = BorderStroke(width = 1.dp, color = colorScheme.outline),
		colors = ButtonColors(
			containerColor = colorScheme.surfaceVariant,
			contentColor = colorScheme.onSurface,
			disabledContainerColor = colorScheme.surfaceVariant,
			disabledContentColor = colorScheme.onSurface,
		)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			Icon(
				modifier = Modifier.size(14.dp),
				painter = painterResource(Res.drawable.pencil),
				contentDescription = "Edit",
				tint = colorScheme.onSurface,
			)
			Spacer(modifier = Modifier.width(8.dp))
			Text(
				text = stringResource(Res.string.main_page_edit_block),
				color = colorScheme.onSurface,
				style = MaterialTheme.typography.bodyMedium
			)
		}
	}
}