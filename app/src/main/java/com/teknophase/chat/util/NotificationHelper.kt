package com.teknophase.chat.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import com.teknophase.chat.R
import com.teknophase.chat.process.ReplyReceiver
import java.util.Date
import javax.inject.Singleton

@Singleton
class NotificationHelper {

    val people = mutableSetOf<Person>()
    val messages = mutableListOf<Message>()

    fun createChannel(context: Context, channelName: String = "default") {
        // Create channel
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(channelName, channelName, importance)
        } else {
            return
        }

        // Register the channel with the system.
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    fun showNotification(
        context: Context,
        channelName: String = "default",
        message: String,
        sender: String
    ) {
        val remoteInput = RemoteInput.Builder("KEY_TEXT_REPLY")
            .build()
        val replyIntent = Intent(context, ReplyReceiver::class.java).apply {
            putExtra("sender", sender)
        }
        val resultPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            replyIntent,
            PendingIntent.FLAG_MUTABLE
        )
        val replyAction = NotificationCompat.Action.Builder(
            android.R.drawable.ic_input_add,
            "REPLY",
            resultPendingIntent
        ).addRemoteInput(remoteInput).build()
        val notificationStyle = NotificationCompat.MessagingStyle(
            Person.Builder().setName(sender).build()
        )
        notificationStyle.conversationTitle = sender

        val notificationBuilder = NotificationCompat.Builder(context, channelName)
            .setSmallIcon(R.drawable.pyng_logo_onboarding)
            .setStyle(notificationStyle)
            .setShortcutId("chat_now")
            .addAction(replyAction)

        notificationBuilder.setGroup("group")

        for (messageItem in messages.filter { it.person?.name == sender })
            notificationStyle.addMessage(messageItem)

        val message = Message(
            message,
            Date().time,
            Person.Builder().setName(sender).build()
        )
        messages.add(message)

        notificationStyle.addMessage(message)
        val notification = notificationBuilder.build()

        var person = people.find { it.name == sender }
        if (person == null) {
            person = Person.Builder().setName(sender).build()
            people.add(person)
        }
        val index = people.indexOf(person)

        val summaryNotification = NotificationCompat.Builder(context, channelName)
            // Set content text to support devices running API level < 24.
            .setContentTitle("${messages.size} message(s) from ${people.size} chat(s)")
            .setContentText("${messages.size} message(s) from ${people.size} chat(s)")
            .setSmallIcon(R.drawable.pyng_logo_onboarding)
            // Build summary info into InboxStyle template.
            .setStyle(
                NotificationCompat.InboxStyle()
                    .setBigContentTitle("2 new messages")
                    .setSummaryText("${messages.size} message(s) from ${people.size} chat(s)")
            )
            // Specify which group this notification belongs to.
            .setGroup("group")
            // Set this notification as the summary for the group.
            .setGroupSummary(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            notificationManager.getNotificationChannel(channelName)
                ?.setConversationId(channelName, sender)
        }
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(index, notification)
            notificationManager.notify(-5, summaryNotification)
        }
    }
}