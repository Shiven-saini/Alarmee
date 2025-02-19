package me.shiven.alarmee.domain.usecase

import android.nfc.Tag
import kotlinx.coroutines.withTimeoutOrNull
import me.shiven.alarmee.domain.dummy.NfcRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetectNfcTagUseCase @Inject constructor(
    private val nfcRepository: NfcRepository
) {
    // Wait for an NFC tag within a specified timeout.
    suspend operator fun invoke(timeoutMillis: Long): Tag? {
        return withTimeoutOrNull(timeoutMillis) {
            nfcRepository.waitForNfcTag(timeoutMillis)
        }
    }
}
