package com.teknophase.chat.viewmodel

import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModel
import com.teknophase.chat.config.AppConfig
import com.teknophase.chat.data.state.BootstrapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BootstrapViewModel @Inject constructor(private val appConfig: AppConfig) : ViewModel() {

    private val bootstrapState: MutableStateFlow<BootstrapState> =
        MutableStateFlow(BootstrapState())
    private var currentAppVersion: Long = 0
    val state: StateFlow<BootstrapState> = bootstrapState

    suspend fun initialize(context: Context) {
        appConfig.initializeNetworkMode(context)
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        currentAppVersion = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode.toLong()
        }
    }

    fun isUpdateRequired(): Boolean {
        bootstrapState.value = bootstrapState.value.copy(isLoading = true)
        val minAppVersion = appConfig.getMinAppVersion()
        var updateRequired = false
        if (minAppVersion != null) updateRequired = currentAppVersion < minAppVersion
        bootstrapState.value = bootstrapState.value.copy(
            isLoading = false,
            isUpdateRequired = updateRequired
        )
        return updateRequired
    }

    fun isUpdateAvailable(): Boolean {
        bootstrapState.value = bootstrapState.value.copy(isLoading = true)
        val latestAppVersion = appConfig.getLatestAppVersion()
        var updateAvailable = false
        if (latestAppVersion != null) updateAvailable = currentAppVersion < latestAppVersion
        bootstrapState.value = bootstrapState.value.copy(
            isLoading = false,
            isUpdateAvailable = updateAvailable
        )
        return updateAvailable
    }


}