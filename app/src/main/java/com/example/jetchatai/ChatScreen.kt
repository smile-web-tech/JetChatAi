package com.example.jetchatai

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun ChatScreen(
    navController: NavController
) {
    Text(text = "Chat")
}

@Composable
@Preview
fun ChatScreenPreview(){
    var fakeNavController = rememberNavController()
    ChatScreen(
        navController = fakeNavController
    )
}