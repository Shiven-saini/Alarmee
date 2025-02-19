package me.shiven.alarmee.data.repository

import me.shiven.alarmee.data.local.qrcode.QrCodeDao
import me.shiven.alarmee.data.local.qrcode.QrCodeEntity
import me.shiven.alarmee.domain.dummy.QrCodeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QrCodeRepositoryImpl @Inject constructor(
    private val qrCodeDao: QrCodeDao
) : QrCodeRepository {

    override suspend fun saveQrCode(text: String) {
        qrCodeDao.insertQrCode(QrCodeEntity(text = text))
    }

    override suspend fun isQrCodePresent(text: String): Boolean {
        // Return true if count is greater than 0.
        return qrCodeDao.getQrCodeCount(text) > 0
    }

    override suspend fun getLatestQrCode() : String? {
        return qrCodeDao.getLatestQrCode()?.text
    }

    override suspend fun isQrCodeDatabaseEmpty(): Boolean {
        return qrCodeDao.getLatestQrCode() == null
    }

}