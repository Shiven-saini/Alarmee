package me.shiven.alarmee.domain.usecase

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.hardware.biometrics.PromptContentItemPlainText
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.shiven.alarmee.domain.dummy.QrCodeRepository
import java.io.IOException
import javax.inject.Inject

class SaveQRCodeUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: QrCodeRepository
) {

    /**
     * Saves the given QR code bitmap to the device’s gallery using the MediaStore API
     * and also stores the associated plainText in the Room database.
     *
     * The operation follows these steps:
     * 1. Save the plainText (obtained during QR code generation) to the Room database.
     *    (If this fails, the bitmap will not be saved.)
     * 2. Save the bitmap image to the MediaStore.
     *
     * If either operation fails, the entire save operation is considered a failure.
     *
     * @param bitmap The QR code image as a Bitmap.
     * @param plainText The plain text that was used to generate the QR code.
     * @return true if both the plainText was saved to the database and the image was saved successfully;
     *         false otherwise.
     */
    suspend operator fun invoke(bitmap: Bitmap, plainText: String): Boolean = withContext(Dispatchers.IO) {
        try {

            // save the plaintext first to the database.
            repository.saveQrCode(plainText)

            // Create a unique filename for the QR code image.
            val filename = "QRCode_${System.currentTimeMillis()}.png"
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                // The image will be saved in the Pictures directory.
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            // Insert the content values to obtain an output URI.
            val resolver = context.contentResolver
            val imageUri: Uri? = resolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )

            // Attempt to compress and save the bitmap.
            imageUri?.let { uri ->
                resolver.openOutputStream(uri)?.use { outStream ->
                    if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)) {
                        return@withContext true
                    } else {
                        throw IOException("Failed to compress bitmap.")
                    }
                }
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}


