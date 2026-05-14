package org.testadirapa.sesterzo.components.mobile.entries

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.input.AmountField
import org.testadirapa.sesterzo.components.input.FormButton
import org.testadirapa.sesterzo.components.input.TextField
import org.testadirapa.sesterzo.components.ui.ModalTitle
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.model.Entry
import org.testadirapa.sesterzo.model.Space
import org.testadirapa.sesterzo.models.FormValue
import org.testadirapa.sesterzo.models.Optional
import org.testadirapa.sesterzo.styles.colors.LocalFinanceColors
import org.testadirapa.sesterzo.styles.typography.amountTextStyleLarge
import org.testadirapa.sesterzo.utils.currentBudgetReference
import org.testadirapa.sesterzo.utils.monthName
import org.testadirapa.sesterzo.validators.MaxLengthOrNullValidator
import org.testadirapa.sesterzo.validators.NonNegativeValidator
import org.testadirapa.sesterzo.validators.defaultNameValidator
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.add_entry_form_title
import sesterzo.composeapp.generated.resources.add_entry_form_type_add_button
import sesterzo.composeapp.generated.resources.add_entry_form_type_amount_label
import sesterzo.composeapp.generated.resources.add_entry_form_type_description_invalid_error
import sesterzo.composeapp.generated.resources.add_entry_form_type_description_label
import sesterzo.composeapp.generated.resources.add_entry_form_type_description_placeholder
import sesterzo.composeapp.generated.resources.add_entry_form_type_expense
import sesterzo.composeapp.generated.resources.add_entry_form_type_income
import sesterzo.composeapp.generated.resources.add_entry_form_type_label
import sesterzo.composeapp.generated.resources.add_entry_form_type_label_invalid_error
import sesterzo.composeapp.generated.resources.add_entry_form_type_label_label
import sesterzo.composeapp.generated.resources.add_entry_form_type_label_placeholder
import sesterzo.composeapp.generated.resources.add_entry_form_type_saving
import sesterzo.composeapp.generated.resources.arrow_back
import sesterzo.composeapp.generated.resources.arrow_forward
import sesterzo.composeapp.generated.resources.banknotes

@Composable
fun AddEntryForm(
	space: Space,
	entryType: Entry.EntryType = Entry.EntryType.Expense,
) {
	var entryTypeState by remember { mutableStateOf(entryType) }
	var amount by remember { mutableStateOf(FormValue(validator = NonNegativeValidator)) }
	var label by remember { mutableStateOf(FormValue(validator = defaultNameValidator)) }
	var description by remember {
		mutableStateOf(
			FormValue(
				value = Optional.Present(null),
				validator = MaxLengthOrNullValidator(300)
			)
		)
	}
	Column(
		modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
	) {
		FormHeader(spaceName = space.name)
		TypeSelector(
			entryType = entryTypeState,
			onEntryTypeChange = { entryTypeState = it },
		)
		Spacer(modifier = Modifier.height(16.dp))
		AmountInput(
			value = amount,
			onAmountChange = { amount = amount.update(it) },
		)
		Spacer(modifier = Modifier.height(16.dp))
		LabelInput(
			value = label,
			onValueChange = { label = label.update(it) },
		)
		Spacer(modifier = Modifier.height(16.dp))
		DescriptionInput(
			value = description,
			onValueChange = { description = description.update(it) },
		)
		Spacer(modifier = Modifier.heightIn(8.dp, 48.dp))
		FormButton(
			onClick = {},
			text = "${stringResource(Res.string.add_entry_form_type_add_button)} ${entryTypeToName(entryTypeState)}",
			enabled = amount.isValid && label.isValid && description.isValid,
			isLoading = false,
			colors = ButtonColors(
				containerColor = colorScheme.primary,
				contentColor = colorScheme.onPrimary,
				disabledContainerColor = colorScheme.surfaceContainerHigh,
				disabledContentColor = colorScheme.onTertiary,
			)
		)
	}
}

@Composable
fun DescriptionInput(
	value: FormValue<String?>,
	onValueChange: (description: String) -> Unit
) {
	Column {
		Text(
			text = stringResource(Res.string.add_entry_form_type_description_label),
			style = MaterialTheme.typography.labelLarge,
			color = colorScheme.onSurfaceVariant,
		)
		Spacer(Modifier.height(4.dp))
		TextField(
			value = value,
			onValueChange = onValueChange,
			title = null,
			multiline = true,
			placeholder = stringResource(Res.string.add_entry_form_type_description_placeholder),
			errorMessage = stringResource(Res.string.add_entry_form_type_description_invalid_error),
			modifier = Modifier.fillMaxWidth().height(150.dp),
		)
	}
}

@Composable
fun LabelInput(
	value: FormValue<String>,
	onValueChange: (label: String) -> Unit
) {
	Column {
		Text(
			text = stringResource(Res.string.add_entry_form_type_label_label),
			style = MaterialTheme.typography.labelLarge,
			color = colorScheme.onSurfaceVariant,
		)
		Spacer(Modifier.height(4.dp))
		TextField(
			value = value,
			onValueChange = onValueChange,
			title = null,
			placeholder = stringResource(Res.string.add_entry_form_type_label_placeholder),
			errorMessage = stringResource(Res.string.add_entry_form_type_label_invalid_error),
			modifier = Modifier.fillMaxWidth().height(75.dp),
		)
	}
}

@Composable
fun AmountInput(
	value: FormValue<Amount>,
	onAmountChange: (amount: Amount) -> Unit
) {
	Column {
		Text(
			text = stringResource(Res.string.add_entry_form_type_amount_label),
			style = MaterialTheme.typography.labelLarge,
			color = colorScheme.onSurfaceVariant,
		)
		Spacer(Modifier.height(4.dp))
		AmountField(
			value = value,
			onValueChange = { newValue ->
				onAmountChange(newValue)
			},
			textStyle = amountTextStyleLarge(),
			backgroundColor = colorScheme.surfaceVariant,
			symbolOffset = 0.dp,
			modifier = Modifier
				.fillMaxWidth()
				.heightIn(60.dp)
		)
	}
}

@Composable
private fun TypeSelector(
	entryType: Entry.EntryType,
	onEntryTypeChange: (entryType: Entry.EntryType) -> Unit,
) {
	Column {
		Text(
			text = stringResource(Res.string.add_entry_form_type_label),
			style = MaterialTheme.typography.labelLarge,
			color = colorScheme.onSurfaceVariant,
		)
		Spacer(Modifier.height(4.dp))
		Card(
			border = BorderStroke(width = 1.dp, color = colorScheme.outline),
			colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
		) {
			Row(
				modifier = Modifier.fillMaxWidth().padding(6.dp),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(8.dp)
			) {
				TypeCard(
					iconPainter = painterResource(Res.drawable.arrow_back),
					iconColor = LocalFinanceColors.current.spent,
					text = stringResource(Res.string.add_entry_form_type_expense),
					isSelected = entryType == Entry.EntryType.Expense,
					modifier = Modifier
						.weight(1f)
						.clickable(onClick = { onEntryTypeChange(Entry.EntryType.Expense)}),
				)
				TypeCard(
					iconPainter = painterResource(Res.drawable.banknotes),
					iconColor = LocalFinanceColors.current.saved,
					text = stringResource(Res.string.add_entry_form_type_saving),
					isSelected = entryType == Entry.EntryType.Saving,
					modifier = Modifier
						.weight(1f)
						.clickable(onClick = { onEntryTypeChange(Entry.EntryType.Saving) }),
				)
				TypeCard(
					iconPainter = painterResource(Res.drawable.arrow_forward),
					iconColor = colorScheme.primary,
					text = stringResource(Res.string.add_entry_form_type_income),
					isSelected = entryType == Entry.EntryType.Income,
					modifier = Modifier
						.weight(1f)
						.clickable(onClick = { onEntryTypeChange(Entry.EntryType.Income) }),
				)
			}
		}
	}
}

@Composable
private fun TypeCard(
	iconPainter: Painter,
	iconColor: Color,
	text: String,
	isSelected: Boolean,
	modifier: Modifier,
) {
	Card(
		border = BorderStroke(
			width = if (isSelected) 1.dp else 0.dp,
			color = if (isSelected) colorScheme.outline else colorScheme.surface
		),
		colors = CardDefaults.cardColors(
			containerColor = if (isSelected) colorScheme.surfaceVariant else colorScheme.surface
		),
		modifier = modifier
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Center,
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 12.dp, horizontal = 12.dp)
		) {
			Icon(
				modifier = Modifier.size(18.dp),
				painter = iconPainter,
				contentDescription = null,
				tint = if (isSelected) iconColor else colorScheme.onSurfaceVariant,
			)
			Spacer(Modifier.width(8.dp))
			Text(
				text = text,
				style = MaterialTheme.typography.labelLarge,
				fontWeight = FontWeight.Bold,
				color = if (isSelected) colorScheme.onSurface else colorScheme.onSurfaceVariant,
			)
		}
	}
}

@Composable
private fun FormHeader(
	spaceName: String,
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 10.dp, bottom = 14.dp),
		verticalAlignment = Alignment.Bottom,
	) {
		val currentReference = currentBudgetReference()
		ModalTitle(
			subtitle = "${monthName(month = currentReference.month, abbreviated = false)} ${currentReference.year}",
			subtitleSpec = spaceName,
			title = stringResource(Res.string.add_entry_form_title),
			modifier = Modifier.weight(1f),
		)
	}
}

@Composable
private fun entryTypeToName(
	entryType: Entry.EntryType
): String = when(entryType) {
	Entry.EntryType.Expense -> stringResource(Res.string.add_entry_form_type_expense)
	Entry.EntryType.Saving -> stringResource(Res.string.add_entry_form_type_saving)
	Entry.EntryType.Income -> stringResource(Res.string.add_entry_form_type_income)
}