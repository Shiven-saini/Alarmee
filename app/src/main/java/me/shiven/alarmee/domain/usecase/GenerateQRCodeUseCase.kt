package me.shiven.alarmee.domain.usecase

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import javax.inject.Inject

class GenerateQRCodeUseCase @Inject constructor(
){

    private val QR_CODE_SIZE = 512

     operator fun invoke(plainText: String): Bitmap {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(
            plainText,
            BarcodeFormat.QR_CODE,
            QR_CODE_SIZE,
            QR_CODE_SIZE
        )
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val color = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
                bitmap.setPixel(x, y, color)
            }
        }
        return bitmap
    }
}
