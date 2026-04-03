package org.testadirapa.sesterzo.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import org.jetbrains.compose.resources.painterResource
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.sesterzo

@Composable
fun LoadingScreen() {
	Box(
		contentAlignment = Alignment.Center,
		modifier = Modifier.fillMaxSize()
	) {
		Image(
			painter = painterResource(Res.drawable.sesterzo),
			contentDescription = null,
			modifier = Modifier.size(240.dp)
		)
	}
}