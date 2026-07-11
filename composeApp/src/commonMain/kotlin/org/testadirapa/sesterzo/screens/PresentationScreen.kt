package org.testadirapa.sesterzo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.testadirapa.sesterzo.components.ui.LogoWithName
import org.testadirapa.sesterzo.styles.colors.LocalFinanceColors
import org.testadirapa.sesterzo.styles.colors.sesterzoGlow
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.presentation_page_point_free
import sesterzo.composeapp.generated.resources.presentation_page_point_free_subtitle
import sesterzo.composeapp.generated.resources.presentation_page_point_oss
import sesterzo.composeapp.generated.resources.presentation_page_point_oss_subtitle
import sesterzo.composeapp.generated.resources.presentation_page_point_secure
import sesterzo.composeapp.generated.resources.presentation_page_point_secure_subtitle
import sesterzo.composeapp.generated.resources.presentation_page_subtitle
import sesterzo.composeapp.generated.resources.presentation_page_title_1
import sesterzo.composeapp.generated.resources.presentation_page_title_2

@Composable
fun PresentationScreen() {
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier
			.fillMaxSize()
			.sesterzoGlow()
	) {
		Column(
			modifier = Modifier
				.padding(all = 72.dp)
				.fillMaxSize(),
		) {
			LogoWithName()
			Spacer(modifier = Modifier.height(72.dp))
			Text(
				text = stringResource(Res.string.presentation_page_title_1),
				style = MaterialTheme.typography.titleLarge,
				fontWeight = FontWeight.Bold,
				fontSize = 48.sp,
				color = colorScheme.onBackground,
			)
			Text(
				text = stringResource(Res.string.presentation_page_title_2),
				style = MaterialTheme.typography.titleLarge,
				fontWeight = FontWeight.Bold,
				fontSize = 48.sp,
				color = colorScheme.primary,
			)
			Text(
				text = stringResource(Res.string.presentation_page_subtitle),
				style = MaterialTheme.typography.bodyLarge,
				fontWeight = FontWeight.Normal,
				fontSize = 20.sp,
				lineHeight = 24.sp,
				letterSpacing = 0.5.sp,
				color = colorScheme.onSurfaceVariant,
			)
			Spacer(modifier = Modifier.height(48.dp))
			FeaturePointRow(
				pointColor = colorScheme.primary,
				text = stringResource(Res.string.presentation_page_point_free),
				subtitle = stringResource(Res.string.presentation_page_point_free_subtitle),
			)
			Spacer(modifier = Modifier.height(8.dp))
			FeaturePointRow(
				pointColor = LocalFinanceColors.current.saved,
				text = stringResource(Res.string.presentation_page_point_secure),
				subtitle = stringResource(Res.string.presentation_page_point_secure_subtitle),
			)
			Spacer(modifier = Modifier.height(8.dp))
			FeaturePointRow(
				pointColor = LocalFinanceColors.current.spent,
				text = stringResource(Res.string.presentation_page_point_oss),
				subtitle = stringResource(Res.string.presentation_page_point_oss_subtitle),
			)
		}
	}
}

@Composable
private fun FeaturePointRow(
	pointColor: Color,
	text: String,
	subtitle: String,
) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		verticalAlignment = Alignment.Top,
	) {
		Column(
			modifier = Modifier.padding(top = 8.dp),
		) {
			Box(
				modifier = Modifier
					.width(12.dp)
					.height(12.dp)
					.background(pointColor, RoundedCornerShape(4.dp))
			)
		}
		Spacer(Modifier.width(8.dp))
		Column {
			Text(
				text = text,
				style = MaterialTheme.typography.bodyLarge,
				fontWeight = FontWeight.Normal,
				fontSize = 20.sp,
				color = colorScheme.onBackground,
			)
			Text(
				text = subtitle,
				style = MaterialTheme.typography.bodyLarge,
				fontWeight = FontWeight.Normal,
				color = colorScheme.onSurfaceVariant,
			)
		}
	}
}