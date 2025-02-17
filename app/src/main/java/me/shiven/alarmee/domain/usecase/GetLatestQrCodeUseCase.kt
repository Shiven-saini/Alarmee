package me.shiven.alarmee.domain.usecase

import me.shiven.alarmee.domain.dummy.QrCodeRepository
import javax.inject.Inject

class GetLatestQrCodeUseCase @Inject constructor(
    private val repository: QrCodeRepository
){
    suspend operator fun invoke() : String? {
        return repository.getLatestQrCode()
    }

}