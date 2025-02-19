package me.shiven.alarmee.ui.alarm

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.shiven.alarmee.data.local.alarm.Alarm
import me.shiven.alarmee.data.repository.AlarmRepository
import me.shiven.alarmee.domain.model.ChallengeList
import me.shiven.alarmee.domain.usecase.AddAlarmUseCase
import me.shiven.alarmee.domain.usecase.CheckQrCodeDatabaseEmptyUseCase
import me.shiven.alarmee.domain.usecase.DeleteAlarmUseCase
import me.shiven.alarmee.domain.usecase.EnableAlarmUseCase
import me.shiven.alarmee.domain.usecase.GenerateQRCodeUseCase
import me.shiven.alarmee.domain.usecase.GetChallengeSelectedUseCase
import me.shiven.alarmee.domain.usecase.GetLatestQrCodeUseCase
import me.shiven.alarmee.domain.usecase.SaveQRCodeUseCase
import me.shiven.alarmee.domain.usecase.SetAlarmChallengeUseCase
import me.shiven.alarmee.domain.usecase.UpdateAlarmDayUseCase
import me.shiven.alarmee.domain.usecase.UpdateAlarmTimeUseCase
import me.shiven.alarmee.domain.usecase.UpdateAlarmToneUseCase
import me.shiven.alarmee.domain.usecase.UpdateChallengeSelectedUseCase
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val enableAlarmUseCase: EnableAlarmUseCase,
    private val addAlarmUseCase: AddAlarmUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val updateAlarmToneUseCase: UpdateAlarmToneUseCase,
    private val updateAlarmDayUseCase: UpdateAlarmDayUseCase,
    private val updateAlarmTimeUseCase: UpdateAlarmTimeUseCase,
    private val setAlarmChallengeUseCase: SetAlarmChallengeUseCase,
    private val generateQRCodeUseCase: GenerateQRCodeUseCase,
    private val saveQRCodeUseCase: SaveQRCodeUseCase,
    private val getLatestQrCodeUseCase: GetLatestQrCodeUseCase,
    private val updateChallengeSelectedUseCase: UpdateChallengeSelectedUseCase,
    private val getChallengeSelectedUseCase: GetChallengeSelectedUseCase,
    private val checkQrCodeDatabaseEmptyUseCase: CheckQrCodeDatabaseEmptyUseCase,
    private val alarmRepository: AlarmRepository
): ViewModel() {

    private val _isDatabaseEmpty = MutableStateFlow<Boolean>(true)
    val isDatabaseEmpty: StateFlow<Boolean> = _isDatabaseEmpty.asStateFlow()

    // State flows to hold the QR bitmap and loading flag.
    private val _qrBitmap = MutableStateFlow<Bitmap?>(null)
    val qrBitmap = _qrBitmap.asStateFlow()

    private val _qrPlainText = MutableStateFlow<String>("")
    val qrPlainText = _qrBitmap.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // SharedFlow to send one-time UI events (e.g., toast messages).
    private val _messageEvent = MutableSharedFlow<String>()
    val messageEvent = _messageEvent.asSharedFlow()

    val selectedChallenge: Flow<ChallengeList> = getChallengeSelectedUseCase()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getLatestQrCodeUseCase()?.let{
                _qrBitmap.value = generateQRCodeUseCase(it)
            }
        }
        checkDatabaseEmpty()

    }

     fun checkDatabaseEmpty() {
        viewModelScope.launch {
            // Invoke the use case to check if the database is empty.
            val empty = checkQrCodeDatabaseEmptyUseCase()
            _isDatabaseEmpty.value = empty
        }
    }

    val alarms: StateFlow<List<Alarm>> = alarmRepository.getAllAlarms()
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun onAlarmEnable(enableStatus: Boolean, alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        enableAlarmUseCase(enableStatus, alarm)
    }

    fun onDaySelectedChange(id: Int, alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        updateAlarmDayUseCase(id, alarm)
    }

    fun onTonePickChange(alarmUri: String, alarm: Alarm?) = viewModelScope.launch(Dispatchers.IO) {
        updateAlarmToneUseCase(alarmUri, alarm)
    }

    fun addAlarm(hour: Int, minute: Int) = viewModelScope.launch(Dispatchers.IO) {
        println("AddAlarm just added an alarm with $hour : $minute")
        addAlarmUseCase(hour, minute)
    }

    fun updateAlarmTime(hour: Int, minute: Int, alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        updateAlarmTimeUseCase(hour, minute, alarm)
    }

    fun deleteAlarm(alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        deleteAlarmUseCase(alarm)
    }

    fun setAlarmChallenge(challengeStatus: Boolean, alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        setAlarmChallengeUseCase(challengeStatus, alarm)
    }

    fun updateChallengeSelected(selectedCardIndex: Int) = viewModelScope.launch(Dispatchers.IO) {
        val challengeSelected = when(selectedCardIndex){
            -1 -> ChallengeList.GRID_GAME
            0 -> ChallengeList.GRID_GAME
            1 -> ChallengeList.NFC_TAG
            2 -> ChallengeList.QR_CODE
            else -> ChallengeList.GRID_GAME
        }
        updateChallengeSelectedUseCase(challengeSelected)
        println("Viewmodel: challenge selected updated => $challengeSelected")
    }

    fun generateQrCode() = viewModelScope.launch(Dispatchers.IO) {
        _isLoading.value = true
        try {
            val randomInt = (0..10000).random()
            val plainText = "Alarmee-$randomInt"
            _qrPlainText.value = plainText
            // Execute code generation off the main thread.
            val bitmap = generateQRCodeUseCase(plainText)
            _qrBitmap.value = bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            _messageEvent.emit("Error generating QR Code")
        } finally {
            _isLoading.value = false
        }
    }

    fun saveQRCode() = viewModelScope.launch {
        _isLoading.value = true
        try {
            val bitmap = _qrBitmap.value
            val plainText = _qrPlainText.value
            if (bitmap != null) {
                val success = saveQRCodeUseCase(bitmap, plainText)
                if (success)
                    _messageEvent.emit("QR Code saved successfully!")
                else
                    _messageEvent.emit("Failed to save QR Code.")
            } else {
                _messageEvent.emit("No QR Code to save.")
            }
            }
        catch (e: Exception) {
            e.printStackTrace()
            _messageEvent.emit("Error during saving QR Code")
        }
        finally {
            _isLoading.value = false
        }
    }
}

val daysMap = mapOf(
    0 to "Sun",
    1 to "Mon",
    2 to "Tue",
    3 to "Wed",
    4 to "Thu",
    5 to "Fri",
    6 to "Sat"
)