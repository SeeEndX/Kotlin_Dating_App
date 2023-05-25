package com.example.laba3


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.laba3.DataBase.DatabaseManager
import com.example.laba3.Screens.crChats
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DatabaseManager.init(applicationContext)
        crChats()
        setContent {
            MainScreen()
        }
    }
}

@ExperimentalPagerApi
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Chats,
        NavigationItem.Profile
    )

    val sysUi = rememberSystemUiController()
    sysUi.isStatusBarVisible = false

    val shouldShowNavigationBar = navController.currentBackStackEntryAsState().value?.destination?.route in items.map { it.route }

     Scaffold(
        bottomBar = { if (shouldShowNavigationBar) BottomNavigationBar(navController, items) },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                Navigation(navController = navController)
            }
        },
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.systemBarsPadding()
    )
}
