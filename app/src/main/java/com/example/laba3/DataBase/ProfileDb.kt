package com.example.laba3.DataBase

import androidx.room.*

@Entity(
    tableName = "profiles",
    indices = [Index("id")],
    foreignKeys = [
        ForeignKey(
            entity = UsersDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProfileDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    val description : String,
    val photoUrl : Int
)