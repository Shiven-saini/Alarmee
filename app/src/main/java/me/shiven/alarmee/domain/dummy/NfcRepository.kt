package me.shiven.alarmee.domain.dummy

import android.nfc.Tag

interface NfcRepository {
    suspend fun waitForNfcTag(timeoutMillis: Long): Tag?
    suspend fun writeTag(tag: Tag, message: String): Boolean
    suspend fun readTag(tag: Tag): String?
}
