package com.example.jetchatai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jetchatai.components.LoginScreen
import com.example.jetchatai.screens.ChatScreen
import com.example.jetchatai.screens.IntroScreen
import com.example.jetchatai.screens.ProfileScreen
import com.example.jetchatai.screens.RegisterScreen
import com.example.jetchatai.screens.VerificationScreen
import com.example.jetchatai.viewmodels.AuthViewModel
import com.example.jetchatai.viewmodels.ChatViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val sharedViewModel: ChatViewModel = viewModel()
            val authViewModel: AuthViewModel = viewModel()
            val chatViewModel: ChatViewModel = viewModel()
            val startDestination = if (authViewModel.currentUser != null) {
                "chat_screen"

            } else {
                "welcome"
            }
            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                composable("welcome") {
                    IntroScreen(navController = navController)
                }

                composable("login") {
                    val authViewModel: AuthViewModel = viewModel()
                    LoginScreen(
                        navController = navController,
                        viewModel = authViewModel
                    )
                }

                composable(
                    "register",
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(400)
                        )
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(400)
                        )
                    }
                ) {
                    RegisterScreen(navController = navController)
                }

                composable(
                    "verif_screen/{email}",
                    enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(400)
                        )
                    }
                ) { backStackEntry ->
                    val email = backStackEntry.arguments?.getString("email") ?: ""
                    val authViewModel: AuthViewModel = viewModel()
                    VerificationScreen(
                        navController = navController,
                        email = email,
                        viewModel = authViewModel

                    )
                }

                composable(
                    "chat_screen",
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None }
                ) {
                    // Pass the sharedViewModel here to keep messages alive
                    ChatScreen(
                        navController = navController,
                        viewModel = sharedViewModel
                    )
                }

                composable(
                    "profile",
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None }
                ) {
                    ProfileScreen(
                        navController = navController
                    )
                }
            }
        }
    }
}