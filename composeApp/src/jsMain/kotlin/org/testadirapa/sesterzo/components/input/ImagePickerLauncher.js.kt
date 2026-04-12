package org.testadirapa.sesterzo.components.input

import androidx.compose.runtime.Composable
import kotlinx.browser.document
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.testadirapa.sesterzo.utils.MAX_IMAGE_SIZE_BYTES
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileReader

@Composable
actual fun rememberImagePickerLauncher(
	onImageSelected: (ByteArray) -> Unit,
	onError: (String) -> Unit,
): () -> Unit {
	return {
		val input = document.createElement("input") as HTMLInputElement
		input.type = "file"
		input.accept = "image/*"
		input.style.display = "none"
		document.body?.appendChild(input)

		input.addEventListener("change", {
			val file = input.files?.item(0)
			document.body?.removeChild(input)
			if (file != null) {
				if (file.size.toLong() > MAX_IMAGE_SIZE_BYTES) {
					onError("Image must be smaller than 10MB")
				} else {
					val reader = FileReader()
					reader.onload = {
						val buffer = reader.result as? ArrayBuffer
						if (buffer != null) {
							@Suppress("UNCHECKED_CAST")
							val bytes = Int8Array(buffer) as ByteArray
							onImageSelected(bytes)
						} else {
							onError("Could not read the selected image")
						}
					}
					reader.readAsArrayBuffer(file)
				}
			}
		})

		input.click()
	}
}
