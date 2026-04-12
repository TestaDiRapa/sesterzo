package org.testadirapa.sesterzo.components.input

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.testadirapa.sesterzo.utils.MAX_IMAGE_SIZE_BYTES

@Composable
actual fun rememberImagePickerLauncher(
	onImageSelected: (ByteArray) -> Unit,
	onError: (String) -> Unit,
): () -> Unit {
	val context = LocalContext.current
	val scope = rememberCoroutineScope()
	val launcher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.GetContent()
	) { uri ->
		if (uri != null) {
			scope.launch(Dispatchers.IO) {
				val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
				withContext(Dispatchers.Main) {
					when {
						bytes == null -> onError("Could not read the selected image")
						bytes.size > MAX_IMAGE_SIZE_BYTES -> onError("Image must be smaller than 10MB")
						else -> onImageSelected(bytes)
					}
				}
			}
		}
	}
	return { launcher.launch("image/*") }
}
