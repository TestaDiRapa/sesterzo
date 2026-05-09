package org.testadirapa.sesterzo.components.template

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.components.input.AmountField
import org.testadirapa.sesterzo.components.input.InlineTextField
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.models.FormValue
import org.testadirapa.sesterzo.styles.typography.amountTextStyleLarge
import org.testadirapa.sesterzo.styles.typography.amountTextStyleMedium
import org.testadirapa.sesterzo.validators.NonNegativeValidator
import org.testadirapa.sesterzo.validators.defaultNameValidator
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.add_source_edit
import sesterzo.composeapp.generated.resources.add_source_page_add_new_source
import sesterzo.composeapp.generated.resources.add_source_page_new_source
import sesterzo.composeapp.generated.resources.minus
import sesterzo.composeapp.generated.resources.plus

@Composable
fun MobileSourceUpdateForm(
	title: String,
	type: String,
	sources: Map<String, Amount>
) {
	val newSource = stringResource(Res.string.add_source_page_new_source)
	var updatedSources by remember {
		mutableStateOf(
			sources.map { (k, v) ->
				FormValue(
					value = k,
					validator = defaultNameValidator,
				) to FormValue(
					value = v,
					validator = NonNegativeValidator
				)
			}
		)
	}
	Column(
		modifier = Modifier
			.padding(horizontal = 16.dp, vertical = 8.dp)
	) {
		FormHeader(
			title = title,
			type = type,
			total = updatedSources.mapNotNull { (_, value) -> value.takeIf { it.isValid } }.sumOf { it.validValue }
		)
		Card(
			modifier = Modifier.fillMaxWidth(),
			border = BorderStroke(width = 1.dp, color = colorScheme.outline),
			colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
		) {
			updatedSources.forEachIndexed { index, (label, amount) ->
				SourceRow(
					label = label,
					value = amount,
					onDelete = {
						updatedSources = updatedSources.filterIndexed { idx, _ ->
							idx != index
						}
					},
					onLabelChange = { newLabel ->
						val tmp = updatedSources.toMutableList()
						tmp[index] = label.update(newLabel) to amount
						updatedSources = tmp
					},
					onAmountChange = { newAmount ->
						val tmp = updatedSources.toMutableList()
						tmp[index] = label to amount.update(newAmount)
						updatedSources = tmp
					}
				)
				if (index != updatedSources.lastIndex) {
					HorizontalDivider(color = colorScheme.outline)
				}
			}
		}
		Row(
			modifier = Modifier
				.clickable(
					onClick = {
						updatedSources = updatedSources + (
							FormValue(value = newSource, validator = defaultNameValidator) to
								FormValue(value = 0, validator = NonNegativeValidator)
						)
					}
				)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp),
		) {
			Icon(
				painter = painterResource(Res.drawable.plus),
				tint = colorScheme.primary,
				contentDescription = null,
				modifier = Modifier.size(12.dp)
			)
			Text(
				text = stringResource(Res.string.add_source_page_add_new_source),
				style = MaterialTheme.typography.titleSmall,
				fontWeight = FontWeight.Bold,
				color = colorScheme.primary,
			)
		}
	}
}

@Composable
private fun SourceRow(
	label: FormValue<String>,
	value: FormValue<Amount>,
	onDelete: () -> Unit,
	onLabelChange: (String) -> Unit,
	onAmountChange: (Amount) -> Unit,
) {
	val inputHeight = 48.dp
	Row(
		modifier = Modifier
			.padding(8.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween,
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
		) {
			MinusButton(onClick = onDelete)
			Spacer(Modifier.width(8.dp))
			InlineTextField(
				value = label,
				onValueChange = onLabelChange,
				placeholder = stringResource(Res.string.add_source_page_new_source),
				modifier = Modifier.width(180.dp).height(inputHeight)
			)
		}
		AmountField(
			value = value,
			onValueChange = { newValue ->
				onAmountChange(newValue)
			},
			textStyle = amountTextStyleMedium(),
			backgroundColor = colorScheme.surfaceVariant,
			modifier = Modifier
				.width(130.dp)
				.height(inputHeight)
		)
	}
}

@Composable
private fun MinusButton(
	onClick: () -> Unit,
) {
	Box(
		Modifier
			.size(24.dp)
			.clip(CircleShape)
			.clickable(onClick = onClick)
			.border(width = 1.dp, color = colorScheme.error, CircleShape)
			.background(colorScheme.onError.copy(alpha = 0.30f), CircleShape),
		contentAlignment = Alignment.Center,
	) {
		Icon(
			modifier = Modifier.size(16.dp),
			painter = painterResource(Res.drawable.minus),
			contentDescription = null,
			tint = colorScheme.error,
		)
	}
}

@Composable
private fun FormHeader(
	title: String,
	type: String,
	total: Amount
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 10.dp, bottom = 14.dp),
		verticalAlignment = Alignment.Bottom,
	) {
		Column(Modifier.weight(1f)) {
			Text(
				text = "${stringResource(Res.string.add_source_edit)} · $type",
				style = MaterialTheme.typography.labelSmall,
				color = colorScheme.onSurfaceVariant,
			)
			Spacer(Modifier.height(4.dp))
			Text(
				text = title,
				fontSize = 17.sp,
				fontWeight = FontWeight.SemiBold,
				color = colorScheme.onSurface,
			)
		}
		Text(
			text = AppCtx.currency.writer(total),
			color = colorScheme.onSurface,
			style = amountTextStyleLarge()
		)
	}
}