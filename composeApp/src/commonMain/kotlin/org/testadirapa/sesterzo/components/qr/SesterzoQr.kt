package org.testadirapa.sesterzo.components.qr

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.qrose.options.QrBallShape
import io.github.alexzhirkevich.qrose.options.QrFrameShape
import io.github.alexzhirkevich.qrose.options.QrLogoPadding
import io.github.alexzhirkevich.qrose.options.QrLogoShape
import io.github.alexzhirkevich.qrose.options.QrPixelShape
import io.github.alexzhirkevich.qrose.options.circle
import io.github.alexzhirkevich.qrose.options.roundCorners
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import org.jetbrains.compose.resources.painterResource
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.sesterzo

@Composable
fun SesterzoQr(
	data: String,
	description: String,
	modifier: Modifier = Modifier
) {
	val painter = rememberQrCodePainter(
		data = data,
		ballShape = QrBallShape.circle(),
		darkPixelShape = QrPixelShape.roundCorners(),
		frameShape = QrFrameShape.roundCorners(.25f),
		logoPainter = painterResource(Res.drawable.sesterzo),
		logoPadding = QrLogoPadding.Natural(.05f),
		logoShape = QrLogoShape.circle(),
		logoSize = 0.3f,
		backgroundFill = SolidColor(colorScheme.onTertiaryContainer)
	)

	Box(
		modifier = modifier
			.background(color = colorScheme.onTertiaryContainer)
			.padding(16.dp),
		contentAlignment = Alignment.Center,
	) {
		Image(painter = painter, contentDescription = description)
	}
}