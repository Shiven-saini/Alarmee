package me.shiven.alarmee.domain.usecase

import me.shiven.alarmee.domain.dummy.QrCodeRepository
import javax.inject.Inject

class CheckQrCodeDatabaseEmptyUseCase @Inject constructor(
    private val repository: QrCodeRepository
) {
    /**
     * Returns true if the QR code database is empty.
     */
    suspend operator fun invoke(): Boolean {
        return repository.isQrCodeDatabaseEmpty()
    }

}