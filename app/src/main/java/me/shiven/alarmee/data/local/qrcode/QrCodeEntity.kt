package me.shiven.alarmee.data.local.qrcode

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "qr_codes")
data class QrCodeEntity(

    @PrimaryKey val text: String,
    val timestamp: Long = System.currentTimeMillis()

)