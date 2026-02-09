package com.example.jetchatai.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jetchatai.R
import com.example.jetchatai.jakarta_bold
import com.example.jetchatai.jakarta_regular
import com.example.jetchatai.ui.theme.LightBackground
import com.example.jetchatai.ui.theme.SolakColor
import com.example.jetchatai.ui.theme.mainColor
import com.example.jetchatai.ui.theme.textColor
import com.example.jetchatai.viewmodels.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val loading by viewModel.isLoading
    val error by viewModel.errorMessage
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
            .background(color = LightBackground),

        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(24.dp))
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.Companion.padding(top = 24.dp)
        )
        Spacer(modifier = Modifier.Companion.height(5.dp))
        Text(
            text = "Welcome Back!",
            fontFamily = jakarta_bold,
            fontWeight = FontWeight.Companion.Bold,
            fontSize = 26.sp
        )
        Spacer(modifier = Modifier.Companion.height(10.dp))
        Text(
            text = "Enter your login details",
            fontFamily = jakarta_regular,
            fontSize = 14.sp,
            color = SolakColor
        )

        if (error != null) {
            Text(
                text = error!!,
                color = Color.Companion.Red,
                fontSize = 12.sp,
                modifier = Modifier.Companion.padding(top = 8.dp)
            )
        }

        Column(
            modifier = Modifier.Companion.padding(top = 24.dp)
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.Companion.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text(text = "Enter Email") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Mail, contentDescription = "null")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLeadingIconColor = mainColor,
                    unfocusedLeadingIconColor = Color.Companion.Gray,
                    focusedLabelColor = mainColor,
                    unfocusedLabelColor = Color.Companion.Gray,

                    )

            )
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = "* Test email: jet@gmail.com",
                fontFamily = jakarta_regular,
                fontSize = 14.sp,
                color = SolakColor
            )

            Spacer(modifier = Modifier.Companion.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.Companion.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                placeholder = { Text(text = "Enter Password") },

                visualTransformation = if (isPasswordVisible) {
                    VisualTransformation.Companion.None
                } else {
                    PasswordVisualTransformation()
                },

                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Companion.Password),

                leadingIcon = {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = "null")
                },

                trailingIcon = {
                    val iconImage = if (isPasswordVisible) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }
                    val description = if (isPasswordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = iconImage,
                            contentDescription = description
                        )
                    }

                },

                colors = OutlinedTextFieldDefaults.colors(

                    focusedLeadingIconColor = mainColor,
                    unfocusedLeadingIconColor = Color.Companion.Gray,
                    focusedLabelColor = mainColor,
                    unfocusedLabelColor = Color.Companion.Gray,
                ),

                )


            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = "* Test password: jet123",
                fontFamily = jakarta_regular,
                fontSize = 14.sp,
                color = SolakColor
            )
            Spacer(modifier = Modifier.Companion.height(16.dp))
            Button(
                onClick = {
                    viewModel.login(email, password) {
                        navController.navigate("chat_screen") {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                },
                enabled = !loading,
                modifier = Modifier.Companion.fillMaxWidth().height(50.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = mainColor,
                )
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        color = Color.Companion.White,
                        modifier = Modifier.Companion.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Login",
                        fontSize = 16.sp,
                        fontFamily = jakarta_regular
                    )
                }
            }

            Row(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.Companion.weight(1f),
                    color = Color.Companion.LightGray,
                    thickness = 1.dp
                )

                Text(
                    text = "Or",
                    color = Color.Companion.Gray,
                    fontFamily = jakarta_regular,
                    modifier = Modifier.Companion.padding(horizontal = 8.dp)
                )

                HorizontalDivider(
                    modifier = Modifier.Companion.weight(1f),
                    color = Color.Companion.LightGray,
                    thickness = 1.dp
                )
            }
            Button(
                onClick = {},
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Companion.White,
                    contentColor = Color.Companion.Black
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.Companion.LightGray
                )
            ) {
                Row(
                    verticalAlignment = Alignment.Companion.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.google),
                        contentDescription = "Google logo",
                        modifier = Modifier.Companion.size(24.dp)
                    )
                    Spacer(modifier = Modifier.Companion.size(12.dp))
                }
                Text(
                    text = "Login with Google",
                    fontFamily = jakarta_regular,
                )

            }
            Spacer(modifier = Modifier.Companion.height(16.dp))
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                verticalAlignment = Alignment.Companion.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Don't have an account? ",
                    fontFamily = jakarta_regular,
                    fontSize = 12.sp
                )
                Text(
                    text = "Sign Up!",
                    color = textColor,
                    modifier = Modifier.Companion.clickable(onClick = {
                        navController.navigate("register")
                    }),
                    fontFamily = jakarta_regular,
                    fontSize = 12.sp
                )
            }

        }


    }

}