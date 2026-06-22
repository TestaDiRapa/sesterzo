package org.testadirapa.sesterzo.styles.typography

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.testadirapa.sesterzo.styles.font.jetBrainsMonoFontFamily

@Composable
fun sesterzoTypography(
	fontFamily: FontFamily,
): Typography = Typography(
	// Screen titles  e.g. "March 2026"
	titleLarge = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Medium, fontSize = 20.sp, lineHeight = 28.sp),
	// Section headers e.g. "Fixed expenses"
	titleMedium = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Medium, fontSize = 15.sp, lineHeight = 22.sp),
	titleSmall = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Medium, fontSize = 13.sp, lineHeight = 20.sp),
	// Body rows
	bodyLarge = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 22.sp),
	bodyMedium = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 20.sp),
	// Muted labels, hex values, section caps
	bodySmall = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Normal, fontSize = 11.sp, lineHeight = 16.sp),
	// Amount values (planned / actual)
	labelLarge = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
	labelMedium = TextStyle(fontFamily = fontFamily, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp),
	labelSmall = TextStyle(
		fontFamily = fontFamily,
		fontWeight = FontWeight.Medium,
		fontSize = 11.sp,
		lineHeight = 14.sp,
		letterSpacing = 0.04.sp
	),
)

@Composable
fun amountTextStyleVeryLarge() = TextStyle(
	fontFamily = jetBrainsMonoFontFamily(),
	fontWeight = FontWeight.Bold,
	fontSize = 27.sp,
	lineHeight = 22.sp
)


@Composable
fun amountTextStyleLarge() = TextStyle(
	fontFamily = jetBrainsMonoFontFamily(),
	fontWeight = FontWeight.Bold,
	fontSize = 18.sp,
	lineHeight = 22.sp
)

@Composable
fun amountTextStyleMedium() = TextStyle(
	fontFamily = jetBrainsMonoFontFamily(),
	fontWeight = FontWeight.Medium,
	fontSize = 15.sp,
	lineHeight = 22.sp
)

@Composable
fun amountTextStyleSmall() = TextStyle(
	fontFamily = jetBrainsMonoFontFamily(),
	fontWeight = FontWeight.Medium,
	fontSize = 13.sp,
	lineHeight = 22.sp
)