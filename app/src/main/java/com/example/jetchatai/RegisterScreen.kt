package com.example.jetchatai

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun RegisterScreen(
    navController: NavController
) {

}

@Composable
@Preview
fun RegisterScreenPreview() {
    val fakeNavController = rememberNavController()
    RegisterScreen(
        navController = fakeNavController
    )
}