package org.testadirapa.sesterzo.components.qr

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import zxingcpp.BarcodeReader
import java.util.concurrent.Executor
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
	val reader = remember {
		BarcodeReader(
			BarcodeReader.Options(
				formats = setOf(BarcodeReader.Format.QR_CODE),
				tryHarder = true,
				tryRotate = true,
				tryInvert = true,
				tryDownscale = true,
				maxNumberOfSymbols = 1,
			)
		)
	}

	DisposableEffect(Unit) {
		onDispose {
			analysisExecutor.shutdown()
		}
	}

	AndroidView(
		modifier = Modifier
			.fillMaxWidth()
			.aspectRatio(1f),
		factory = { ctx ->
			val previewView = PreviewView(ctx)
			val mainExecutor = ContextCompat.getMainExecutor(ctx)
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
							processImage(imageProxy, reader, handled, mainExecutor, onResult)
						}
					}
				cameraProvider.unbindAll()
				cameraProvider.bindToLifecycle(
					lifecycleOwner,
					CameraSelector.DEFAULT_BACK_CAMERA,
					preview,
					analysis,
				)
			}, mainExecutor)
			previewView
		},
	)
}

private fun processImage(
	imageProxy: ImageProxy,
	reader: BarcodeReader,
	handled: AtomicBoolean,
	callbackExecutor: Executor,
	onResult: (String) -> Unit,
) {
	imageProxy.use {
		if (handled.get()) return
		// zxing-cpp decodes synchronously on the analysis thread; hand the result back on the main
		// thread so the callback can safely touch compose state.
		val value = runCatching { reader.read(it) }.getOrNull()
			?.firstNotNullOfOrNull { result -> result.text?.takeIf(String::isNotEmpty) }
		if (value != null && handled.compareAndSet(false, true)) {
			callbackExecutor.execute { onResult(value) }
		}
	}
}
