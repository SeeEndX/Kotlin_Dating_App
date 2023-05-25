package com.example.laba3.DataBase

import androidx.room.*

//реализация DAO интерфейса для запросов
@Dao
interface UserDao{
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UsersDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: UsersDbEntity)

    @Query("SELECT * FROM users WHERE user_name = :username AND password = :password")
    suspend fun getUserByUsernameAndPassword(username: String, password: String): UsersDbEntity?

    @Query("SELECT user_name FROM users WHERE id = :userId")
    suspend fun getUsernameByProfileId(userId: Int): String

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCount(): Int

    @Delete
    suspend fun deleteUser(user: UsersDbEntity)
}

@Dao
interface ChatDao{
    @Query("SELECT * FROM chats WHERE user1_id = :userId OR user2_id = :userId")
    suspend fun getChatsForUser(userId: Int): List<ChatsDbEntity>

    @Query("SELECT * FROM chats WHERE id = :chatId")
    suspend fun getChatById(chatId: Int): ChatsDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChat(chat: ChatsDbEntity)

    @Delete
    suspend fun deleteChat(chat: ChatsDbEntity)
}


@Dao
interface MessageDao{
    @Query("SELECT * FROM messages WHERE chat_id = :chatId")
    suspend fun getMessagesByChatId(chatId: Int): List<MessagesDbEntity>

    @Query("SELECT COUNT(*) FROM messages")
    fun getMessageCount(): Int

    @Query("SELECT * FROM messages WHERE id = :mesId")
    fun getMessageById(mesId: Int): MessagesDbEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMessage(message: MessagesDbEntity)

    @Delete
    suspend fun deleteMessage(message: MessagesDbEntity)
}

@Dao
interface ProfileDao{
    @Query("SELECT * FROM profiles WHERE user_id = :userId")
    suspend fun getProfileByUserId(userId: Int): ProfileDbEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProfile(profile: ProfileDbEntity)
}