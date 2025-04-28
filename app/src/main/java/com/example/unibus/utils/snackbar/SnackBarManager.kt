package com.example.unibus.utils.snackbar

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


object SnackBarManager {
    private var _messages: MutableStateFlow<SnackBarMessage?> = MutableStateFlow(null)

    val snackBarMessages: StateFlow<SnackBarMessage?> get() = _messages.asStateFlow()

    fun showMessage(@StringRes message: Int) {
        _messages.value = SnackBarMessage.ResourceSnackBar(message)
    }

    fun showMessage(snackBarMessage: SnackBarMessage) {
        _messages.value = snackBarMessage
    }

    fun clearSnackBarState() {
        _messages.value = null
    }

    fun showMessage(s: String) {

    }
}