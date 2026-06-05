package org.testadirapa.sesterzo.components.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.testadirapa.sesterzo.components.input.FormButton
import org.testadirapa.sesterzo.model.Currency
import org.testadirapa.sesterzo.model.User

@Composable
fun UserCurrencyUpdateForm(
	user: User,
	buttonLabel: String,
	isLoading: Boolean,
	onSubmit: (currency: Currency) -> Unit,
) {
	var selectedCurrency by remember { mutableStateOf(user.preferredCurrency) }
	Column(
		verticalArrangement = Arrangement.spacedBy(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Currency.entries.chunked(3).forEach { entries ->
			Row(
				modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(8.dp),
			) {
				entries.forEach { entry ->
					CurrencyButton(
						currency = entry,
						isSelected = entry == selectedCurrency,
						onClick = { selectedCurrency = entry },
					)
				}
			}
		}
		FormButton(
			text = buttonLabel,
			isLoading = isLoading,
			onClick = {
				onSubmit(selectedCurrency)
			}
		)
	}
}

@Composable
fun CurrencyButton(
	currency: Currency,
	isSelected: Boolean,
	onClick: () -> Unit,
) {
	Card(
		modifier = Modifier
			.width(105.dp)
			.height(68.dp)
			.clickable(onClick = onClick),
		colors =
			if (isSelected) {
				CardDefaults.cardColors(containerColor = colorScheme.primary)
			} else {
				CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant)
			}
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center,
			modifier = Modifier.fillMaxSize()
		) {
			Text(
				text = "${currency.symbol} ${currency.name}",
				style = MaterialTheme.typography.bodySmall,
				fontWeight = FontWeight.Bold,
				color =
					if (isSelected) {
						colorScheme.onPrimary
					} else {
						colorScheme.onSurface
					},
			)
		}
	}
}