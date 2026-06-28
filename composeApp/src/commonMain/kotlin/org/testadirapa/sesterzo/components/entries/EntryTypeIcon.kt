package org.testadirapa.sesterzo.components.entries

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.styles.colors.LocalFinanceColors
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.arrow_back
import sesterzo.composeapp.generated.resources.arrow_forward
import sesterzo.composeapp.generated.resources.banknotes

@Composable
fun EntryTypeIcon(entryType: Entry.EntryType) {
	val painter = when (entryType) {
		Entry.EntryType.Expense -> painterResource(Res.drawable.arrow_back)
		Entry.EntryType.Income -> painterResource(Res.drawable.arrow_forward)
		Entry.EntryType.Saving -> painterResource(Res.drawable.banknotes)
	}
	val (backgroundColor, contentColor) = when (entryType) {
		Entry.EntryType.Expense -> colorScheme.onError.copy(alpha = 0.30f) to colorScheme.error
		Entry.EntryType.Income -> colorScheme.primaryContainer to colorScheme.onPrimaryContainer
		Entry.EntryType.Saving -> LocalFinanceColors.current.savedBg.copy(alpha = 0.30f) to LocalFinanceColors.current.saved
	}
	Box(
		Modifier
			.size(32.dp)
			.clip(RoundedCornerShape(8.dp))
			.background(backgroundColor, RoundedCornerShape(8.dp))
			.border(1.dp, contentColor, RoundedCornerShape(8.dp)),
		contentAlignment = Alignment.Center,
	) {
		Icon(
			modifier = Modifier.size(16.dp),
			painter = painter,
			contentDescription = null,
			tint = contentColor,
		)
	}
}