package org.testadirapa.sesterzo.screens.main.desktop.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.testadirapa.sesterzo.components.input.EditButton
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.DecryptedEntry
import org.testadirapa.sesterzo.model.Entry

@Composable
fun DesktopBudgetDetailsScreen(
	label: String,
	color: Color,
	type: Entry.EntryType,
	scheduled: Map<String, Amount>,
	entries: List<DecryptedEntry>,
) {
	Column(
		modifier = Modifier.padding(all = 32.dp),
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween,
		) {
			DetailsHeader(label = label, color = color)
			if (type != Entry.EntryType.Income) {
				EditButton(onEdit = {})
			}
		}

	}
}

@Composable
private fun SummaryCards(
	scheduled: Map<String, Amount>,
	entries: List<DecryptedEntry>
) {

}

@Composable
private fun SummaryCard(
	label: String,
	amount: Amount,
	color: Color
) {
	Card(

	) {
		
	}
}

@Composable
private fun DetailsHeader(
	label: String,
	color: Color,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
	) {
		Box(
			modifier = Modifier
				.width(7.dp)
				.height(7.dp)
				.background(color, RoundedCornerShape(1.dp))
		)
		Spacer(modifier = Modifier.width(8.dp))
		Text(
			text = label,
			color = colorScheme.onBackground,
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.Bold
		)
	}
}