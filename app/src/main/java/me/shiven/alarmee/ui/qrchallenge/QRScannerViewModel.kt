package me.shiven.alarmee.ui.qrchallenge

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class QRScannerViewModel @Inject constructor(): ViewModel() {
    private val _scannedCode = MutableStateFlow<String?>(null)
    val scannedCode: StateFlow<String?> = _scannedCode

    fun onQRCodeScanned(code: String) {
        _scannedCode.value = code
    }
}
