package me.shiven.alarmee.domain.dummy

interface QrCodeRepository {
    suspend fun saveQrCode(text: String)
    suspend fun isQrCodePresent(text: String): Boolean
    suspend fun getLatestQrCode() : String?
    suspend fun isQrCodeDatabaseEmpty(): Boolean
}