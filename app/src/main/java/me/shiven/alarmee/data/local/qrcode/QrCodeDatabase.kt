package me.shiven.alarmee.data.local.qrcode

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [QrCodeEntity::class], version = 2, exportSchema = false)
abstract class QrCodeDatabase : RoomDatabase() {
    abstract fun qrCodeDao(): QrCodeDao
}