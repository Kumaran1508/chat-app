package com.teknophase.chat.util


import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Enumeration

fun getLocalIpAddress(): String? {
    try {
        val interfaces: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
        while (interfaces.hasMoreElements()) {
            val networkInterface: NetworkInterface = interfaces.nextElement()
            val addresses: Enumeration<InetAddress> = networkInterface.inetAddresses
            while (addresses.hasMoreElements()) {
                val address: InetAddress = addresses.nextElement()
                if (!address.isLoopbackAddress && address.isSiteLocalAddress) {
                    return address.hostAddress
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}


fun main() {
    // Usage Test
    val ipAddress = getLocalIpAddress()
    if (ipAddress != null) {
        println("IP Address: $ipAddress")
    } else {
        println("Failed to retrieve IP address")
    }
}