package me.shiven.alarmee.domain.usecase

import android.nfc.Tag
import me.shiven.alarmee.domain.dummy.NfcRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgramNfcTagUseCase @Inject constructor(
    private val nfcRepository: NfcRepository
) {
    // Write the "Alarmee" message to the detected NFC tag.
    suspend operator fun invoke(tag: Tag): Boolean {
        return nfcRepository.writeTag(tag, "Alarmee")
    }
}
