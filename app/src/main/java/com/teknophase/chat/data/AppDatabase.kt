package com.teknophase.chat.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.teknophase.chat.data.model.ChatListModel
import com.teknophase.chat.data.model.Message
import com.teknophase.chat.data.room.LocalChatListRepository
import com.teknophase.chat.data.room.LocalMessageRepository
import com.teknophase.chat.util.RoomTypeConvertors

@Database(
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ],
    entities = [Message::class, ChatListModel::class]
)
@TypeConverters(RoomTypeConvertors::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun messageRepository(): LocalMessageRepository
    abstract fun chatListRepository(): LocalChatListRepository

}

object AppDatabase {
    var db: LocalDatabase? = null
    fun init(applicationContext: Context) {
        db = Room.databaseBuilder(
            applicationContext,
            LocalDatabase::class.java, "pyng"
        ).build()
    }
}