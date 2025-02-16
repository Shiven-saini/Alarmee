package me.shiven.alarmee.ui.qrchallenge.components

import android.content.Context
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@Composable
fun CameraPreviewCompose(
    modifier: Modifier = Modifier,
    lensFacing: Int = CameraSelector.LENS_FACING_BACK
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    // Create a single-thread executor for image analysis.
    val analysisExecutor = remember { Executors.newSingleThreadExecutor() }

    // Use a PreviewView to display the camera feed.
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).also { previewView = it }
        },
        modifier = modifier.background(androidx.compose.ui.graphics.Color.Black)
    )

    LaunchedEffect(lensFacing, previewView) {
        if (previewView == null) return@LaunchedEffect

        // Get the cameraProvider instance.
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()

        // Build the Preview use case.
        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(previewView?.surfaceProvider)
        }

        // Create the ImageAnalysis use case and attach an analyzer.
        val imageAnalysis = ImageAnalysis.Builder()
            // Use the latest frame to avoid backlog.
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        imageAnalysis.setAnalyzer(analysisExecutor) { imageProxy ->
            processImageProxy(imageProxy, context)
        }

        // Build the camera selector.
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        // It is best to unbind all use-cases before rebinding.
        cameraProvider.unbindAll()
        try {
            // Bind the Preview and ImageAnalysis use cases.
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

// Helper function to process each frame and detect QR codes.
@OptIn(ExperimentalGetImage::class)
private fun processImageProxy(imageProxy: ImageProxy, context: Context) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        // Convert mediaImage to ML Kit’s InputImage.
        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )
        // Configure the barcode scanner to only detect QR codes.
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        val scanner = BarcodeScanning.getClient(options)

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                barcodes.forEach { barcode ->
                    barcode.rawValue?.let { value ->
                        // Show the detected QR code value in a Toast.
                        Toast.makeText(context, "QR Code: $value", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
            .addOnCompleteListener {
                // It is important to close the imageProxy when done.
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}
