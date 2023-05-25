package com.example.laba3

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.laba3.DataBase.DatabaseManager
import com.example.laba3.DataBase.ProfileDbEntity

@Composable
//fun createProfileScreen(name : String, description : String){
fun createProfileScreen(profile: ProfileDbEntity){
    var username by remember { mutableStateOf("") }

    LaunchedEffect(Unit){
        username = DatabaseManager.getAppDatabase().userDao().getUsernameByProfileId(profile.userId)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .clickable {
                },
            shape = RoundedCornerShape(15.dp),
            elevation = 5.dp,
            backgroundColor = Color(0xFF3057CC)
        ){
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text(text = username, fontSize = 22.sp, color = Color(0xFFFFFFFF))
            }
        }
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
                .clickable {
                },
            shape = RoundedCornerShape(15.dp),
            elevation = 5.dp,
            backgroundColor = Color(0xFF151E3A)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(5.dp)
            ) {
                Image(
                    painter = painterResource(id = profile.photoUrl),
                    "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(512.dp)
                        .clip(RoundedCornerShape(10))
                        .padding(20.dp)
                )
                Text(text = profile.description, fontSize = 16.sp, color = Color(0xFFFFFFFF))
            }
        }
    }
}