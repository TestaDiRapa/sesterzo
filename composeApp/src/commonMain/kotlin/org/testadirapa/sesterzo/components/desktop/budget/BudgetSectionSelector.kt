package org.testadirapa.sesterzo.components.desktop.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.testadirapa.sesterzo.AppCtx
import org.testadirapa.sesterzo.model.Amount
import org.testadirapa.sesterzo.styles.typography.amountTextStyleLarge
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.arrow_right

@Composable
fun BudgetSectionSelector(
	label: String,
	isSelected: Boolean,
	totalAmount: Amount,
	painter: Painter,
	color: Color,
	onClick: () -> Unit,
) {
	Card(
		modifier = Modifier.clickable(onClick = onClick),
		colors = CardDefaults.cardColors(
			containerColor = if (isSelected) colorScheme.surfaceVariant else colorScheme.surface,
		),
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(12.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween,
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
			) {
				Spacer(Modifier.width(4.dp))
				Box(
					Modifier
						.size(48.dp)
						.clip(RoundedCornerShape(12.dp))
						.background(colorScheme.surfaceContainerHigh),
					contentAlignment = Alignment.Center,
				) {
					Icon(
						modifier = Modifier.size(24.dp),
						painter = painter,
						contentDescription = null,
						tint = color,
					)
				}
				Spacer(Modifier.width(16.dp))
				Text(
					text = label,
					color = colorScheme.onSurfaceVariant,
					fontSize = 17.sp,
					fontWeight = FontWeight.Normal,
				)
			}
			Row(
				verticalAlignment = Alignment.CenterVertically,
			) {
				Text(
					text = AppCtx.currency.writer(totalAmount),
					color = colorScheme.onSurface,
					style = amountTextStyleLarge()
				)
				Icon(
					modifier = Modifier.size(24.dp),
					painter = painterResource(Res.drawable.arrow_right),
					contentDescription = "Next",
					tint = colorScheme.onSurfaceVariant,
				)
			}
		}
	}
}