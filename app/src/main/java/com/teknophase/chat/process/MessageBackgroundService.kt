package com.teknophase.chat.process

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.teknophase.chat.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MessageBackgroundService: Service() {

    @Inject
    lateinit var homeViewModel: HomeViewModel
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        homeViewModel.initialize()
        return START_STICKY
    }
}