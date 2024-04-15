package com.teknophase.chat.process

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.RemoteInput
import com.teknophase.chat.util.NotificationHelper
import javax.inject.Inject

class ReplyReceiver @Inject constructor(private val notificationHelper: NotificationHelper) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)


        val sender = intent.getStringExtra("sender").toString()
        Log.i("NotificationReceiver","sender: $sender")
        Log.i("NotificationReceiver","sender: ${remoteInput?.getString("sender").toString()}")

        // Todo: Send Message

        if (remoteInput != null) {
            val title = remoteInput.getCharSequence(
                "KEY_TEXT_REPLY"
            ).toString()
            Log.i("NotificationReceiver", title) // we will just log the user input for now
            notificationHelper.showNotification(context= context, message = title, sender = sender)
        }
    }
}