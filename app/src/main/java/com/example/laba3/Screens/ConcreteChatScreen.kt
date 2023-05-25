package com.example.laba3.Screens

import  android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.laba3.DataBase.ChatsDbEntity
import com.example.laba3.DataBase.DatabaseManager
import com.example.laba3.R
import com.example.laba3.firebasedb.*
import java.text.SimpleDateFormat
import java.util.*

private val ChatShapeLeft = RoundedCornerShape(0.dp,8.dp,8.dp,8.dp)
private val ChatShapeRight = RoundedCornerShape(8.dp,0.dp,8.dp,8.dp)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ConcreteChatScreen(chatId : Int, navController: NavController) {
    var showNavBar by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Chat $chatId")
                },
                backgroundColor = Color(0xFF3057CC),
                contentColor = Color(0xFFFFFFFF),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = {
            MainChatScreen(userId = 1, chatId = chatId)
        }
    )

    LaunchedEffect(Unit) {
        showNavBar = false
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun MainChatScreen(userId:Int, chatId:Int) {
    val chatMessages = remember { mutableStateListOf<Messages>() }
    var newMessageSent by remember { mutableStateOf(false) }
    var messageCount by remember { mutableStateOf(0L) }
    var currentChat : ChatsDbEntity?
    var username by remember { mutableStateOf("") }

    getMessageCount { count ->
        messageCount = count
    }

    fetchDataFromFirebase()
    when (val result = response.value){
        is DataState.Loading->{
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }
        is DataState.Success->{
            Column (
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
            ){
                TopBarSection(
                    username = username,
                    profile = painterResource(id = R.drawable.image3),
                )

                LaunchedEffect(Unit){
                    currentChat = DatabaseManager.getAppDatabase().chatDao().getChatById(chatId)
                    username = if (currentChat!!.user2Id != userId )
                        DatabaseManager.getAppDatabase().userDao().getUserById(currentChat!!.user2Id)!!.username
                    else
                        DatabaseManager.getAppDatabase().userDao().getUserById(currentChat!!.user1Id)!!.username
                    chatMessages.addAll(result.data)
                }
                LaunchedEffect(newMessageSent) {
                    if (newMessageSent) {
                        val newMessage = result.data.lastOrNull()
                        if (newMessage != null) {
                            chatMessages.add(newMessage)
                        }
                        newMessageSent = false
                    }
                }

                ChatSection(Modifier.weight(1f), chatMessages,chatId, userId)
                MessageSection(chatId, onMessageSent = { newMessageSent = true }, chatMessages, messageCount)
            }
        }
        is DataState.Failure->{
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(text = result.message, fontSize = 20.sp)
            }
        }
        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(text = "Возникла ошибка в загрузке сообщений", fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun ChatSection(
    modifier: Modifier = Modifier,
    messages: MutableList<Messages>,
    chatId: Int,
    ownerId: Int
){
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        reverseLayout = false
    ){
        items(messages){message ->
            if (message.chatId == chatId) {
                MessageItem(
                    messageText = message.text,
                    isOut = ownerId == message.senderId,
                    date = message.date,
                    chatId = chatId
                )
            }
        }
    }
}

@Composable
fun MessageItem(
    messageText: String,
    date: Long?,
    isOut: Boolean,
    chatId: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isOut) Alignment.End else Alignment.Start
        ) {
        if (messageText != null && messageText != ""){
            Box(
                modifier = Modifier
                    .background(
                        Color(0xFF18368F),
                        shape = if (isOut) ChatShapeRight else ChatShapeLeft
                    )
                    .padding(20.dp, 5.dp, 10.dp, 5.dp)
            ) {
                Text(text = messageText, color = Color.White, fontSize = 24.sp)
            }
                Text(text = formatDateTime(date), color = Color(0xFF000000), fontSize = 16.sp)
        }
    }
}

@Composable
fun MessageSection(
    chatId: Int,
    onMessageSent: () -> Unit,
    chatMessages: MutableList<Messages>,
    messageCount : Long
) {
    val messageRef = remember { mutableStateOf("") }
    val message = messageRef.value

    TextField(
        value = message,
        onValueChange = { messageRef.value = it },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Send
        ),
        keyboardActions = KeyboardActions(
            onSend = {
                val newMessage = Messages(
                    id = messageCount.toInt() + 1,
                    chatId = chatId,
                    senderId = 1,
                    senderName = "SeeEnd",
                    text = message,
                    date = System.currentTimeMillis()
                )
                addMessageToFirebase(newMessage)
                chatMessages.remove(newMessage)
                messageRef.value = ""
                onMessageSent()
            }
        ),
        placeholder = { Text(text = "Введите сообщение...") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun TopBarSection(
    username: String,
    profile: Painter
){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        backgroundColor = Color(0xFF738ACF)
    ) {
        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
                ){
            Image(
                painter = profile,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            
            Spacer(modifier = Modifier.width(10.dp))
            
            Column {
                Text(text = username, fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
            }
        }
    }
}

private fun formatDateTime(dateInMillis: Long?): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(dateInMillis!!))
}
