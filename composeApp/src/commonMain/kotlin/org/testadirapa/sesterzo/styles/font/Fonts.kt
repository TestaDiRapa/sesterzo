package org.testadirapa.sesterzo.styles.font

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import sesterzo.composeapp.generated.resources.Res
import sesterzo.composeapp.generated.resources.dm_sans_bold
import sesterzo.composeapp.generated.resources.dm_sans_medium
import sesterzo.composeapp.generated.resources.dm_sans_regular
import sesterzo.composeapp.generated.resources.jetbrains_mono_bold
import sesterzo.composeapp.generated.resources.jetbrains_mono_medium
import sesterzo.composeapp.generated.resources.jetbrains_mono_regular
import sesterzo.composeapp.generated.resources.jetbrains_mono_semibold

@Composable
fun dmSansFontFamily(): FontFamily = FontFamily(
	Font(Res.font.dm_sans_regular, FontWeight.Normal),
	Font(Res.font.dm_sans_medium, FontWeight.Medium),
	Font(Res.font.dm_sans_bold, FontWeight.Bold),
)

@Composable
fun jetBrainsMonoFontFamily(): FontFamily = FontFamily(
	Font(Res.font.jetbrains_mono_regular, FontWeight.Normal),
	Font(Res.font.jetbrains_mono_medium, FontWeight.Medium),
	Font(Res.font.jetbrains_mono_bold, FontWeight.Bold),
	Font(Res.font.jetbrains_mono_semibold, FontWeight.SemiBold),
)