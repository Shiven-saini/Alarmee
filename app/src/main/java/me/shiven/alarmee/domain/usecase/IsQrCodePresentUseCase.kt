package me.shiven.alarmee.domain.usecase

import me.shiven.alarmee.domain.dummy.QrCodeRepository
import javax.inject.Inject

class IsQrCodePresentUseCase @Inject constructor(
    private val repository: QrCodeRepository
) {
    // Returns true if the given QR code text is present in the database.
    suspend operator fun invoke(text: String): Boolean {
        return repository.isQrCodePresent(text)
    }

}