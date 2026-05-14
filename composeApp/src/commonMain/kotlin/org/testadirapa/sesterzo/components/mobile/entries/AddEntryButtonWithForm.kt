package org.testadirapa.sesterzo.components.mobile.entries

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.testadirapa.sesterzo.model.Space
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntryButtonWithForm(
	space: Space
) {
	var showDialog by remember { mutableStateOf(false) }
	val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	Button(
		onClick = {
			showDialog = !showDialog
		},
		shape = CircleShape,
		contentPadding = PaddingValues(0.dp),
		modifier = Modifier.size(60.dp),
		colors = ButtonColors(
			containerColor = colorScheme.primary,
			contentColor = colorScheme.onPrimary,
			disabledContainerColor = colorScheme.surfaceContainerHigh,
			disabledContentColor = colorScheme.onTertiary,
		)
	) {
		Icon(
			modifier = Modifier.size(45.dp),
			tint = colorScheme.onPrimary,
			painter = painterResource(Res.drawable.plus),
			contentDescription = null,
		)
	}

	if (showDialog) {
		ModalBottomSheet(
			onDismissRequest = { showDialog = false },
			sheetState = sheetState,
			containerColor = colorScheme.surface,
		) {
			AddEntryForm(
				space = space,
			)
		}
	}
}