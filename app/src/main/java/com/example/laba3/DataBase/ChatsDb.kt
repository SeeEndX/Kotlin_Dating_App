package com.example.laba3.DataBase

import androidx.room.*

//чаты
@Entity(
    tableName = "chats",
    indices = [Index("id")],
    foreignKeys = [
        ForeignKey(
            entity = UsersDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["user1_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UsersDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["user2_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ChatsDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "chat_name") val chatName: String,
    @ColumnInfo(name = "user1_id") val user1Id: Int,
    @ColumnInfo(name = "user2_id") val user2Id: Int
)
