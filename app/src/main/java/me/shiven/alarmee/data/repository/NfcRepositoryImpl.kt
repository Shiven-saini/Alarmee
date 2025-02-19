package me.shiven.alarmee.data.repository

import android.content.Context
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import me.shiven.alarmee.domain.dummy.NfcRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class NfcRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NfcRepository {

    private val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)

    // Suspends until an NFC tag is detected or the process is cancelled.
    override suspend fun waitForNfcTag(timeoutMillis: Long): Tag? {
        return suspendCancellableCoroutine { cont ->
            // Set up your foreground dispatch system and register a callback to listen for NFC intents.
            // This pseudo-code represents setting up the listener.
            val callback = object : NfcCallback {
                override fun onTagDetected(tag: Tag) {
                    if (!cont.isCompleted) {
                        cont.resume(tag)
                    }
                }
            }
            registerNfcCallback(callback)
            cont.invokeOnCancellation {
                unregisterNfcCallback(callback)
            }
            // The timeout is managed by the use case using withTimeoutOrNull.
        }
    }

    override suspend fun writeTag(tag: Tag, message: String): Boolean {
        return try {
            val ndef = Ndef.get(tag)
            ndef.connect()
            val mimeRecord = NdefRecord.createMime("text/plain", message.toByteArray(Charsets.UTF_8))
            val ndefMessage = NdefMessage(arrayOf(mimeRecord))
            ndef.writeNdefMessage(ndefMessage)
            ndef.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Read a text message from an NFC tag.
    override suspend fun readTag(tag: Tag): String? {
        return try {
            val ndef = Ndef.get(tag)
            ndef.connect()
            val ndefMessage = ndef.ndefMessage
            ndef.close()

            if (ndefMessage == null || ndefMessage.records.isEmpty()) {
                null
            } else {
                // For simplicity, we assume the first record contains the text.
                val record = ndefMessage.records.first()
                when {
                    // Case for MIME media type (e.g., text/plain).
                    record.tnf == NdefRecord.TNF_MIME_MEDIA && record.type.decodeToString() == "text/plain" -> {
                        String(record.payload, Charsets.UTF_8)
                    }
                    // Case for well-known text record.
                    record.tnf == NdefRecord.TNF_WELL_KNOWN && record.type.contentEquals(NdefRecord.RTD_TEXT) -> {
                        decodeTextRecord(record)
                    }
                    else -> null
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    // Helper method to decode a well-known text record.
    private fun decodeTextRecord(record: NdefRecord): String? {
        return try {
            val payload = record.payload
            // The first byte contains the "status byte" which encodes the text encoding and language code length.
            val textEncoding = if (payload[0].toInt() and 0x80 == 0) Charsets.UTF_8 else Charsets.UTF_16
            val languageCodeLength = payload[0].toInt() and 0x3F
            // The text itself starts after the language code.
            String(payload, languageCodeLength + 1, payload.size - languageCodeLength - 1, textEncoding)
        } catch (e: Exception) {
            null
        }
    }

    // Pseudo-functions for registering and unregistering NFC callbacks.
    private fun registerNfcCallback(callback: NfcCallback) {
        // Integrate Android's NFC adapter foreground dispatch here.
    }

    private fun unregisterNfcCallback(callback: NfcCallback) {
        // Remove NFC callback registration.
    }
}

interface NfcCallback {
    fun onTagDetected(tag: Tag)
}
