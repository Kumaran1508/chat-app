package com.teknophase.chat.data.state

data class BootstrapState(
    val isLoading: Boolean = false,
    val isUpdateRequired: Boolean = false,
    val isUpdateAvailable: Boolean = false
)
