package me.shiven.alarmee.ui.alarm

import android.app.Application
import android.content.Context
import android.nfc.Tag
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.shiven.alarmee.domain.usecase.DetectNfcTagUseCase
import me.shiven.alarmee.domain.usecase.ProgramNfcTagUseCase
import javax.inject.Inject

@HiltViewModel
class NfcViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val detectNfcTagUseCase: DetectNfcTagUseCase,
    private val programNfcTagUseCase: ProgramNfcTagUseCase
) : ViewModel() {

    // Expose a state for the current programming status.
    private val _programState = MutableStateFlow(NfcProgramState.Loading)
    val programState: StateFlow<NfcProgramState> = _programState

    // Manage the visibility of the dialog.
    val showDialog = MutableStateFlow(true)

    fun startNfcProcess() {
        viewModelScope.launch {
            try {
                // Wait up to 60 seconds for a tag.
                val tag: Tag? = detectNfcTagUseCase(30000L)
                if (tag == null) {
                    // If no tag is detected, notify the user and dismiss the dialog.
                    Toast.makeText(context, "No NFC tag detected", Toast.LENGTH_LONG).show()
                    showDialog.value = false
                    return@launch
                }
                Toast.makeText(context, "NFC Tag detected", Toast.LENGTH_SHORT).show()

                // Program the tag with "Alarmee"
                val success = programNfcTagUseCase(tag)
                if (success) {
                    Toast.makeText(context, "NFC Tag programmed successfully", Toast.LENGTH_SHORT).show()
                    _programState.value = NfcProgramState.Success
                    // Keep the success icon visible for a short moment.
                    delay(1000L)
                    showDialog.value = false
                } else {
                    _programState.value = NfcProgramState.Error
                    Toast.makeText(context, "Error programming tag. Please try a different tag.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                _programState.value = NfcProgramState.Error
                Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}