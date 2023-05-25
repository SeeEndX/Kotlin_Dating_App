package com.example.laba3.DataBase

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE

//сообщения
@Entity(
    tableName = "messages",
    indices = [Index("id")],
    foreignKeys = [
        ForeignKey(
            entity = UsersDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["sender_id"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = ChatsDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["chat_id"],
            onDelete = CASCADE
        )
    ]
)
data class MessagesDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "chat_id") val chatId: Int,
    @ColumnInfo(name = "sender_id") val senderId: Int,
    @ColumnInfo(name = "sender_name") val senderName: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "date") val date: Long
)