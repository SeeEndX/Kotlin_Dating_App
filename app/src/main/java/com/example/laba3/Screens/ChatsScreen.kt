package com.example.laba3.Screens

import android.icu.util.Calendar
import android.provider.ContactsContract.Data
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.laba3.DataBase.*
import com.example.laba3.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ChatListScreen(navHostController: NavHostController) {
    val chats = remember { mutableStateListOf<ChatsDbEntity>() }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            chats.clear() //не было
            val chatsFromDb = DatabaseManager.getChatsForUser(1)
            chats.addAll(chatsFromDb)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    )  {
        Text(text = "Мои чаты", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        LazyColumn {
            items(chats) { chat ->
                ChatListItem(chat, navHostController)
            }
        }
    }
}

@Composable
fun ChatListItem(chat: ChatsDbEntity, navHostController: NavHostController) {
    Card (
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .padding(2.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp,
        backgroundColor = Color(0xFF3057CC)
    )
    {
        Box(
            Modifier.clickable(onClick = {
                navHostController.navigate("chat/${chat.id}")
            })
        ) {
            Row() {
                Image(
                    painter = painterResource(id = R.drawable.image3),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(64.dp)
                        .clip(CircleShape)
                )
                Text(text = chat.chatName, fontSize = 20.sp, color = Color(0xFFFFFFFF))
            }
        }
    }
}

fun crChats(){
    val calendar = Calendar.getInstance()
    calendar.set(2023,Calendar.MAY,13, 6, 0)

    val user1 = UsersDbEntity(1, "SeeEnd","1234")
    val user2 = UsersDbEntity(2,"Rayan","123")
    val user3 = UsersDbEntity(3, "Cat", "4321")

    val chat = ChatsDbEntity(1,"SeeEnd+Rayan",user1.id, user2.id)
    val chat2 = ChatsDbEntity(2,"Cat+SeeEnd",user3.id, user1.id)

//    val messages = listOf(
//        MessagesDbEntity(3, 1, user1.id, user1.username, "Hello",calendar.timeInMillis),
//        MessagesDbEntity(2, 1, user2.id, user2.username, "Hi",calendar.timeInMillis),
//        MessagesDbEntity(1, 1, user2.id, user2.username, "How are u?",calendar.timeInMillis),
//        MessagesDbEntity(5, 2, user3.id, user3.username, "Hello",calendar.timeInMillis),
//        MessagesDbEntity(4, 2, user1.id, user1.username, "Bye.",calendar.timeInMillis)
//    )

    CoroutineScope(Dispatchers.IO).launch {
        DatabaseManager.getAppDatabase().userDao().addUser(user1)
        DatabaseManager.getAppDatabase().userDao().addUser(user2)
        DatabaseManager.getAppDatabase().userDao().addUser(user3)

        DatabaseManager.getAppDatabase().chatDao().addChat(chat)
        DatabaseManager.getAppDatabase().chatDao().addChat(chat2)

//        for (message in messages){
//            DatabaseManager.getAppDatabase().messageDao().addMessage(message)
//        }

        DatabaseManager.getAppDatabase().chatDao().getChatsForUser(user1.id)
    }


}
