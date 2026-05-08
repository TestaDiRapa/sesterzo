package org.testadirapa.sesterzo.models

import androidx.compose.ui.graphics.Color
import org.testadirapa.sesterzo.model.Amount

data class BudgetCategory(
	val label: String,
	val amount: Amount,
	val color: Color,
)
