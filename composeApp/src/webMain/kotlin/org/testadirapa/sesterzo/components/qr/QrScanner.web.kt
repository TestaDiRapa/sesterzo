package org.testadirapa.sesterzo.components.qr

import androidx.compose.runtime.Composable

@Composable
actual fun QrScanner(onResult: (String) -> Unit) {
	throw UnsupportedOperationException("QR scanning is not supported on browser")
}