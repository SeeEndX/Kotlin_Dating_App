package com.example.laba3.firebasedb

//data class Messages(var id: Int, var chatId: Int,var senderId:Int, var senderName : String, var text: String, var date: Long?)

data class Messages(
    var id: Int = 0,
    var chatId: Int = 0,
    var senderId: Int = 0,
    var senderName: String = "",
    var text: String = "",
    var date: Long = 0
)
