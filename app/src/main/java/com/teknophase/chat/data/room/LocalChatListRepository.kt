package com.teknophase.chat.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.teknophase.chat.data.model.ChatListModel
import com.teknophase.chat.data.model.Message

@Dao
interface LocalChatListRepository {
    @Query("SELECT * from ChatListModel")
    fun getAll() : List<ChatListModel>

    @Insert
    fun save(chatListModel: ChatListModel)

    @Update
    fun update(chatListModel: ChatListModel)
}