package me.shiven.alarmee.data.local.qrcode

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QrCodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQrCode(qrCode: QrCodeEntity)

    // Returns the count; if > 0 then the QR code is present.
    @Query("SELECT COUNT(*) FROM qr_codes WHERE text = :qrText")
    suspend fun getQrCodeCount(qrText: String): Int

    @Query("SELECT * FROM qr_codes ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestQrCode(): QrCodeEntity?

}