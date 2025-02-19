package me.shiven.alarmee.ui.nfcchallenge

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import me.shiven.alarmee.data.receiver.NFCStateReceiver
import me.shiven.alarmee.domain.usecase.ReadNfcTagUseCase
import javax.inject.Inject

@HiltViewModel
class NFCViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val readNfcTagUseCase: ReadNfcTagUseCase
) : ViewModel() {

    private val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)

    // StateFlow to hold NFC enabled/disabled state
    private val _isNFCEnabled = MutableStateFlow(nfcAdapter?.isEnabled ?: false)
    val isNFCEnabled: StateFlow<Boolean> = _isNFCEnabled

    // StateFlow to check if NFC is supported on the device
    private val _isNFCSupported = MutableStateFlow(nfcAdapter != null)
    val isNFCSupported: StateFlow<Boolean> = _isNFCSupported

    private val _nfcContent = MutableStateFlow<String?>(null)
    val nfcContent: StateFlow<String?> = _nfcContent

    // Flow to emit a success event once the valid tag is detected.
    private val _nfcSuccessEvent = MutableSharedFlow<Unit>(replay = 0)
    val nfcSuccessEvent = _nfcSuccessEvent.asSharedFlow()

    // Flow to emit toast messages.
    private val _toastEvent = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val toastEvent = _toastEvent.asSharedFlow()

    // Starts continuous scanning until a valid "Alarmee" tag is detected.
    fun startScanningForNfcTag() {
        viewModelScope.launch {
            while (true) {
                // Wait indefinitely and read the tag's content.
                val content = readNfcTagUseCase.invoke()
                if (content == "Alarmee") {
                    // Valid tag found; emit success and exit the loop.
                    _nfcSuccessEvent.emit(Unit)
                    break
                } else {
                    // Emit a toast event for an invalid tag.
                    _toastEvent.emit("Not a valid Alarmee Tag")
                    // Optionally, add a delay to avoid rapid re-triggering.
                    delay(500L)
                }
            }
        }
    }

    fun readNfcTag(timeoutMillis: Long = 10000L) {
        viewModelScope.launch {
            // Call the use-case.
            val content = readNfcTagUseCase()
            _nfcContent.value = content
        }
    }


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
