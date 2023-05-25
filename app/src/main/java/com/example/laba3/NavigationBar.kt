package com.example.laba3

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.laba3.DataBase.ProfileDbEntity
import com.example.laba3.Screens.AuthScreen
import com.example.laba3.Screens.ChatListScreen
import com.example.laba3.Screens.ConcreteChatScreen
import com.google.accompanist.pager.ExperimentalPagerApi

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItem("home", R.drawable.home, "Home")
    object Chats : NavigationItem("chats", R.drawable.chat, "Chats")
    object Profile : NavigationItem("profile", R.drawable.profile, "Profile")
    object Auth : NavigationItem("auth", 0, "Auth")
}


@Composable
fun BottomNavigationBar(navController: NavController, items: List<NavigationItem>) {

    BottomNavigation(
        backgroundColor = Color(0xFF08278D),
        modifier = Modifier.height(90.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title, fontSize = 19.sp) },
                selectedContentColor = Color(0xFFC5CDE4),
                unselectedContentColor = Color(0xFF4E6FD3),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalPagerApi
@Composable
fun Navigation(navController: NavHostController) {

    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Auth.route) {
            AuthScreen(navController)
        }
        composable(NavigationItem.Home.route) {
            HomeScreen()
        }
        composable(NavigationItem.Chats.route) {
            ChatListScreen(navController)
        }
        composable(NavigationItem.Profile.route) {
            createProfileScreen(ProfileDbEntity(1,1, "android developer+Firebase+Room+Compose",R.drawable.image1))
        }
        composable("chat/{chatId}", arguments = listOf(navArgument("chatId") { type = NavType.IntType })) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getInt("chatId") ?: 0
            ConcreteChatScreen(chatId, navController)
        }
    }
}