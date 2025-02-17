package me.shiven.alarmee.ui.qrchallenge.components

import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.shiven.alarmee.ui.qrchallenge.QRScannerViewModel
import java.util.concurrent.Executors

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    onDismissClick: () -> Unit,
    viewModel: QRScannerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    // Create a single-thread executor for image analysis.
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }
    DisposableEffect(Unit) {
        onDispose { analysisExecutor.shutdown() }
    }

    // Create the ML Kit BarcodeScanner instance only once.
    val scanner = remember {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        BarcodeScanning.getClient(options)
    }
    DisposableEffect(scanner) {
        onDispose { scanner.close() }
    }

    // State to hold the current QR code and a Job to reset the state.
    var currentQRCode by remember { mutableStateOf<String?>(null) }
    var resetJob by remember { mutableStateOf<Job?>(null) }

    // When currentQRCode changes, show toast just once.
    LaunchedEffect(currentQRCode) {
        currentQRCode?.let { qrValue ->
            val isQrCodeValid = viewModel.isQrCodeValid(qrValue)
            if(isQrCodeValid) {
                onDismissClick()
            } else {
                Toast.makeText(context, "Not a valid Alarmee QR Code!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // Create a PreviewView to display the camera feed.
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).also { previewView = it }
        },
        modifier = modifier.background(Color.Black)
    )

    LaunchedEffect(lensFacing, previewView) {
        if (previewView == null) return@LaunchedEffect

        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        val preview = Preview.Builder()
            .build()
            .apply {
                setSurfaceProvider(previewView?.surfaceProvider)
            }

        val imageAnalysis = ImageAnalysis.Builder()
            // Use only the latest frame to avoid backlog.
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(analysisExecutor) { imageProxy ->
            processImageProxy(
                imageProxy = imageProxy,
                scanner = scanner,
                onQRCodeDetected = { qrValue ->
                    coroutineScope.launch {
                        // Cancel any pending reset.
                        resetJob?.cancel()
                        // If a new (different) QR code is detected, update the state.
                        if (qrValue != currentQRCode) {
                            currentQRCode = qrValue
                        }
                    }
                },
                onNoQRCode = {
                    coroutineScope.launch {
                        // If no QR code is detected, schedule a timeout to reset the state.
                        if (currentQRCode != null) {
                            resetJob?.cancel()
                            resetJob = launch {
                                delay(1000) // Wait for 1 second of no detection.
                                currentQRCode = null
                            }
                        }
                    }
                }
            )
        }

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        cameraProvider.unbindAll()
        try {
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
        } catch (exc: Exception) {
            exc.printStackTrace()
        }
    }
}

@OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    imageProxy: ImageProxy,
    scanner: com.google.mlkit.vision.barcode.BarcodeScanner,
    onQRCodeDetected: (String) -> Unit,
    onNoQRCode: () -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        // Convert the media image to an InputImage.
        val inputImage = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )
        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isEmpty()) {
                    onNoQRCode()
                } else {
                    barcodes.forEach { barcode ->
                        barcode.rawValue?.let { value ->
                            onQRCodeDetected(value)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
            .addOnCompleteListener {
                // Always close the image to allow the next frame to be processed.
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}
