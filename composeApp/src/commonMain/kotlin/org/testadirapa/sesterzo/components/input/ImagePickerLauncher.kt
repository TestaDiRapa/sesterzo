package org.testadirapa.sesterzo.components.input

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePickerLauncher(
	onImageSelected: (ByteArray) -> Unit,
	onError: (String) -> Unit,
): () -> Unit
