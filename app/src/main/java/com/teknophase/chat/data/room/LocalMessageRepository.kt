package com.teknophase.chat.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.teknophase.chat.data.model.Message

@Dao
interface LocalMessageRepository {
    @Query("SELECT * from Message")
    fun getAll() : List<Message>

    @Insert
    fun save(message: Message)

    @Update
    fun update(message: Message)

    @Delete
    fun delete(message: Message)
}