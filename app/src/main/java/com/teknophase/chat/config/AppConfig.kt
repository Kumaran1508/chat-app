package com.teknophase.chat.config

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.teknophase.chat.network.repositories.api.AppConfigRepository
import com.teknophase.chat.util.NetworkChangeReceiver
import com.teknophase.chat.util.isConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppConfig @Inject constructor(private val appConfigRepository: AppConfigRepository) {
    private val offlineMode: MutableState<Boolean> = mutableStateOf(true)
    private var minAppVersion: Long? = null
    private var latestAppVersion: Long? = null
    private lateinit var context: Context
    private lateinit var networkChangeReceiver: NetworkChangeReceiver

    fun isOfflineMode(): State<Boolean> {
        return offlineMode
    }

    fun getLatestAppVersion(): Long? {
        return latestAppVersion
    }

    fun getMinAppVersion(): Long? {
        return minAppVersion
    }

    suspend fun initializeNetworkMode(context: Context) {
        offlineMode.value = !isConnected(context = context)
        this.context = context
        networkChangeReceiver = NetworkChangeReceiver(
            onNetworkConnected = { onNetworkConnected() },
            onNetworkDisconnected = { onNetworkDisconnected() }
        )
        if (!offlineMode.value) {
            try {
                minAppVersion = appConfigRepository.getMinAppVersion()
                latestAppVersion = appConfigRepository.getLatestAppVersion()
            } catch (e: Exception) {
                Log.e("AppConfig","Error getting update info")
            }
        }
    }

    private fun onNetworkConnected() {
        offlineMode.value = true
        context.let {
            GlobalScope.launch(Dispatchers.IO) {
                minAppVersion = appConfigRepository.getMinAppVersion()
                latestAppVersion = appConfigRepository.getLatestAppVersion()
            }
            Log.d("AppConfig", "Min: $minAppVersion")
            Log.d("AppConfig", "Latest: $latestAppVersion")
        }
    }

    private fun onNetworkDisconnected() {
        offlineMode.value = false
    }


}

