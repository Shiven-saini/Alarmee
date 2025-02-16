package me.shiven.alarmee.ui.nfcchallenge

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.provider.Settings
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import me.shiven.alarmee.data.receiver.NFCStateReceiver
import javax.inject.Inject

@HiltViewModel
class NFCViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)

    // StateFlow to hold NFC enabled/disabled state
    private val _isNFCEnabled = MutableStateFlow(nfcAdapter?.isEnabled ?: false)
    val isNFCEnabled: StateFlow<Boolean> = _isNFCEnabled

    // StateFlow to check if NFC is supported on the device
    private val _isNFCSupported = MutableStateFlow(nfcAdapter != null)
    val isNFCSupported: StateFlow<Boolean> = _isNFCSupported

    private val nfcStateReceiver = NFCStateReceiver { isEnabled ->
        _isNFCEnabled.value = isEnabled
    }

    init {
        // Register the receiver if NFC is available on the device.
        if (nfcAdapter != null) {
            val intentFilter = IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED)
            context.registerReceiver(nfcStateReceiver, intentFilter)
        }
    }

    // Function to open NFC settings
    fun openNFCSettings() {
        val intent = Intent(Settings.ACTION_NFC_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun onCleared() {
        super.onCleared()
        // Unregister the BroadcastReceiver to prevent memory leaks.
        if (nfcAdapter != null) {
            context.unregisterReceiver(nfcStateReceiver)
        }
    }
}
