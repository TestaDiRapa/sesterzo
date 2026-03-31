package org.testadirapa.sesterzo.styles.typography

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val SesterzoTypography = Typography(
	// Screen titles  e.g. "March 2026"
	titleLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp, lineHeight = 28.sp),
	// Section headers e.g. "Fixed expenses"
	titleMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 15.sp, lineHeight = 22.sp),
	titleSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 13.sp, lineHeight = 20.sp),
	// Body rows
	bodyLarge = TextStyle(fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 22.sp),
	bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 20.sp),
	// Muted labels, hex values, section caps
	bodySmall = TextStyle(fontWeight = FontWeight.Normal, fontSize = 11.sp, lineHeight = 16.sp),
	// Amount values (planned / actual)
	labelLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
	labelMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp),
	labelSmall = TextStyle(
		fontWeight = FontWeight.Medium,
		fontSize = 11.sp,
		lineHeight = 14.sp,
		letterSpacing = 0.04.sp
	),
)
