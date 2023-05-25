package com.example.laba3.DataBase

import androidx.room.*

//Пати чатов
@Entity(
    tableName = "party",
    indices = [Index("id")],
    foreignKeys = [
        ForeignKey(
            entity = ChatsDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["chat1_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ChatsDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["chat2_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PartyDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "chat1_id") val chat1Id: String,
    @ColumnInfo(name = "chat2_id") val chat2Id: Int
)