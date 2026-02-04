package com.example.jetchatai.screens

import android.widget.Toast
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jetchatai.R
import com.example.jetchatai.ui.theme.LightBackground
import com.example.jetchatai.ui.theme.mainColor
import com.example.jetchatai.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen(
    navController: NavController,
    email: String = "your-email@gmail.com",
    viewModel: AuthViewModel = viewModel()
) {
    var code by remember { mutableStateOf("") }
    val loading by viewModel.isLoading
    val context = LocalContext.current
    LaunchedEffect(code) {
        if (code.length == 6) {
            viewModel.verifyCode(code) {
                Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
                navController.navigate("chat_screen") {
                    // Clear the backstack so they can't go back to the code screen
                    popUpTo("register") { inclusive = true }
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightBackground)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBackground)
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Enter the 6-digit code",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "We sent a code to $email",
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))
            BasicTextField(
                value = code,
                onValueChange = {
                    if (it.length <= 6) {
                        code = it
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                decorationBox = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                    ) {
                        repeat(6) { index ->
                            val char = when {
                                index >= code.length -> ""
                                else -> code[index].toString()
                            }
                            Box(
                                modifier = Modifier
                                    .size(height = 64.dp, width = 50.dp)
                                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                                    .border(
                                        width = 1.dp,
                                        color = if (index < code.length) mainColor else Color.LightGray,
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = char,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            )
            if (viewModel.errorMessage.value != null) {
                Text(
                    text = viewModel.errorMessage.value!!,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 16.dp).align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun VerifPreview() {
    val fakeNavController = rememberNavController()
    VerificationScreen(
        navController = fakeNavController
    )
}