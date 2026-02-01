package com.example.jetchatai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
            setContent {
                val navController = rememberNavController()
                val sharedViewModel: ChatViewModel = viewModel()
                NavHost(
                    navController = navController,
                    startDestination = "welcome"
                ) {
                    composable("welcome") {
                        IntroScreen(
                            navController = navController,
                        )
                    }
                    composable("login") {
                        LoginScreen(
                            navController = navController,
                        )
                    }
                }
            }
    }
}
