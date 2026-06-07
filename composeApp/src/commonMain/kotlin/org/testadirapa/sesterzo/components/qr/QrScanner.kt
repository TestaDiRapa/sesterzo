package org.testadirapa.sesterzo.components.qr

import androidx.compose.runtime.Composable

@Composable
expect fun QrScanner(onResult: (String) -> Unit)