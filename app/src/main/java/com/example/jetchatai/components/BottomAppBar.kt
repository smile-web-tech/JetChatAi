package com.example.jetchatai.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jetchatai.R
import com.example.jetchatai.jakarta_regular
import com.example.jetchatai.ui.theme.mainColor

@Composable
fun BottomAppBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Chatbot,
        BottomNavItem.Profile
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.shadow(
            elevation = 15.dp,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        )
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true

                    }
                },
                label = {
                    Text(
                        text = item.label,
                        fontFamily = jakarta_regular,
                        color = if (selected) mainColor else Color.Gray,
                        fontSize = 12.sp
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp),
                        tint = if (selected) mainColor else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent // Removes the default oval background
                )
            )
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: Int, val label: String) {
    object Chatbot : BottomNavItem("chat_screen", R.drawable.robot, "Chatbot")
    object Profile : BottomNavItem("profile", R.drawable.user, "Profile")
}