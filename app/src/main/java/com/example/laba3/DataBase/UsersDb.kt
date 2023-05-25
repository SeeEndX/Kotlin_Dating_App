package com.example.laba3.DataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

//пользователи
@Entity(
    tableName = "users"
)
data class UsersDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "user_name")
    val username: String,
    val password: String
)
