package org.testadirapa.sesterzo.components.qr

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

@Composable
actual fun QrScanner(onResult: (String) -> Unit) {
	val context = LocalContext.current
	var hasPermission by remember {
		mutableStateOf(
			ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
				PackageManager.PERMISSION_GRANTED
		)
	}
	val permissionLauncher = rememberLauncherForActivityResult(
		ActivityResultContracts.RequestPermission()
	) { granted -> hasPermission = granted }

	LaunchedEffect(Unit) {
		if (!hasPermission) {
			permissionLauncher.launch(Manifest.permission.CAMERA)
		}
	}

	if (hasPermission) {
		CameraPreview(onResult = onResult)
	}
}

@Composable
private fun CameraPreview(onResult: (String) -> Unit) {
	val lifecycleOwner = LocalLifecycleOwner.current
	val analysisExecutor = remember { Executors.newSingleThreadExecutor() }
	val handled = remember { AtomicBoolean(false) }
	val scanner = remember {
		BarcodeScanning.getClient(
			BarcodeScannerOptions.Builder()
				.setBarcodeFormats(Barcode.FORMAT_QR_CODE)
				.build()
		)
	}

	DisposableEffect(Unit) {
		onDispose {
			analysisExecutor.shutdown()
			scanner.close()
		}
	}

	AndroidView(
		modifier = Modifier
			.fillMaxWidth()
			.aspectRatio(1f),
		factory = { ctx ->
			val previewView = PreviewView(ctx)
			val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
			cameraProviderFuture.addListener({
				val cameraProvider = cameraProviderFuture.get()
				val preview = Preview.Builder().build().also {
					it.surfaceProvider = previewView.surfaceProvider
				}
				val analysis = ImageAnalysis.Builder()
					.setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
					.build()
					.also { imageAnalysis ->
						imageAnalysis.setAnalyzer(analysisExecutor) { imageProxy ->
							processImage(imageProxy, scanner, handled, onResult)
						}
					}
				cameraProvider.unbindAll()
				cameraProvider.bindToLifecycle(
					lifecycleOwner,
					CameraSelector.DEFAULT_BACK_CAMERA,
					preview,
					analysis,
				)
			}, ContextCompat.getMainExecutor(ctx))
			previewView
		},
	)
}

@OptIn(ExperimentalGetImage::class)
private fun processImage(
	imageProxy: ImageProxy,
	scanner: BarcodeScanner,
	handled: AtomicBoolean,
	onResult: (String) -> Unit,
) {
	val mediaImage = imageProxy.image
	if (mediaImage == null || handled.get()) {
		imageProxy.close()
		return
	}
	val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
	scanner.process(image)
		.addOnSuccessListener { barcodes ->
			val value = barcodes.firstNotNullOfOrNull { it.rawValue }
			if (value != null && handled.compareAndSet(false, true)) {
				onResult(value)
			}
		}
		.addOnCompleteListener { imageProxy.close() }
}