package me.shiven.alarmee.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter

class NFCStateReceiver(
    private val onNFCStateChanged: (Boolean) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == NfcAdapter.ACTION_ADAPTER_STATE_CHANGED) {
            // Retrieve the current state of the NFC adapter from the intent.
            val state = intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE, NfcAdapter.STATE_OFF)
            // Call the callback with true if NFC is on, false otherwise.
            onNFCStateChanged(state == NfcAdapter.STATE_ON)
        }
    }
}
