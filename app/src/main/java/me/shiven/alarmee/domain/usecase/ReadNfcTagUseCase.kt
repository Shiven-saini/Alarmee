package me.shiven.alarmee.domain.usecase

import kotlinx.coroutines.withTimeoutOrNull
import me.shiven.alarmee.domain.dummy.NfcRepository
import javax.inject.Inject

class ReadNfcTagUseCase @Inject constructor(
    private val nfcRepository: NfcRepository
) {
    // This call will suspend until an NFC tag is detected, and then reads its text.
    suspend operator fun invoke(): String? {
        // The repository's waitForNfcTag uses a cancellable coroutine; passing Long.MAX_VALUE effectively waits indefinitely.
        val tag = nfcRepository.waitForNfcTag(timeoutMillis = Long.MAX_VALUE)
        return tag?.let { nfcRepository.readTag(it) }
    }
}
