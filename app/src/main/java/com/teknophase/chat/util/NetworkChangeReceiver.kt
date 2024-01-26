package com.teknophase.chat.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkChangeReceiver(
    private val onNetworkConnected: () -> Unit,
    private val onNetworkDisconnected: () -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (isConnected(context)) {
            onNetworkConnected.invoke()
        } else {
            onNetworkDisconnected.invoke()
        }
    }
}

fun isConnected(context: Context?): Boolean {
    val connectivityManager =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

    val network = connectivityManager?.activeNetwork
    val networkCapabilities = connectivityManager?.getNetworkCapabilities(network)

    return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        ?: false
}
