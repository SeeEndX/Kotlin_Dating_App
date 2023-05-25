package com.example.laba3.DataBase

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//версия и сущности БД
@Database(
    version = 1,
    entities = [
        UsersDbEntity::class,
        ChatsDbEntity::class,
        MessagesDbEntity::class
    ]
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun chatDao() : ChatDao
    abstract fun messageDao(): MessageDao
}

object DatabaseManager {
    private lateinit var db: AppDataBase

    fun init(context: Context) {
        db = Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "datingapp"
        )
            .build()
    }

    fun getAppDatabase(): AppDataBase {
        if (!::db.isInitialized) {
            throw Exception("AppDatabase has not been initialized.")
    }
        return db
    }

    suspend fun getChatsForUser(userId: Int): List<ChatsDbEntity> {
        return withContext(Dispatchers.IO) {
            getAppDatabase().chatDao().getChatsForUser(userId)
        }
    }

    suspend fun getMessagesByChatId(chatId: Int) : List<MessagesDbEntity>{
        return withContext(Dispatchers.IO) {
            getAppDatabase().messageDao().getMessagesByChatId(chatId)
        }
    }
}

