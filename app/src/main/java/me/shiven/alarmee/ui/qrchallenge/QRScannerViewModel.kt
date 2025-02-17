package me.shiven.alarmee.ui.qrchallenge

import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.shiven.alarmee.domain.dummy.QrCodeRepository
import javax.inject.Inject

@HiltViewModel
class QRScannerViewModel @Inject constructor(
    private val repository: QrCodeRepository
): ViewModel() {
    private val _scannedCode = MutableStateFlow<String?>(null)
    val scannedCode: StateFlow<String?> = _scannedCode

    suspend fun isQrCodeValid(code: String): Boolean {
        return withContext(Dispatchers.IO) {
            repository.isQrCodePresent(code)
        }
    }

}
