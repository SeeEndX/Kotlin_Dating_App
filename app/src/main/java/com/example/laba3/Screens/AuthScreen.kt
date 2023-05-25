package com.example.laba3.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.laba3.DataBase.DatabaseManager
import com.example.laba3.NavigationItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun AuthScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF2F4F8)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Login") },
            modifier = Modifier
                .fillMaxWidth(0.75f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(0.75f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (isValidCredentials(username, password)) {
                    navController.navigate(NavigationItem.Home.route) {
                        // Очистка стека навигации, чтобы предотвратить возврат на экран аутентификации
                        popUpTo(navController.graph.startDestinationRoute!!) {
                            inclusive = true
                        }
                    }
                } else {
                    errorMessage = "Неверные данные, попробуйте снова"
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.25f)
        ) {
            Text(text = "Войти")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (errorMessage.isNotBlank()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}


private fun isValidCredentials(username: String, password: String): Boolean {
    var isExist = false
    CoroutineScope(Dispatchers.IO).launch{
        val user = DatabaseManager.getAppDatabase().userDao().getUserByUsernameAndPassword(username, password)
        if (user != null && username.isNotBlank() && password.isNotBlank()) isExist = true
    }

    return isExist
}