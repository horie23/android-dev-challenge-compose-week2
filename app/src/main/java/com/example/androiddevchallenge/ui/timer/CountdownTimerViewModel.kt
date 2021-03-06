package com.example.androiddevchallenge.ui.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch

class CountdownTimerViewModel : ViewModel() {

    private val _countdownTime = MutableLiveData<Long>()
    val countdownTime: LiveData<Long> = _countdownTime

    private val _isCountingDown = MutableLiveData<Boolean>()
    val isCountingDown: LiveData<Boolean> = _isCountingDown

    var ticker: ReceiveChannel<Unit>? = null
    var countdownJob: Job? = null

    fun startCountdown(time: Long) {
        _isCountingDown.value = true
        _countdownTime.value = time
        countdownJob = viewModelScope.launch {
            ticker = ticker(delayMillis = 1000, initialDelayMillis = 0)
            ticker?.consumeEach {
                val nextTime = _countdownTime.value?.minus(1) ?: 1000
                _countdownTime.value = nextTime
                // 0になったら終了
                if (nextTime <= 0) {
                    stopCountdown()
                }
            }
            stopCountdown()
        }
    }

    fun stopCountdown() {
        _isCountingDown.value = false
        countdownJob?.cancel()
    }
}
