package org.testadirapa.sesterzo.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.styles.colors.LocalFinanceColors

@Composable
fun entryTypeColor(type: Entry.EntryType): Color = when (type) {
	Entry.EntryType.Income -> MaterialTheme.colorScheme.primary
	Entry.EntryType.Expense -> LocalFinanceColors.current.spent
	Entry.EntryType.Saving -> LocalFinanceColors.current.saved
}
