package com.example.jetchatai

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jetchatai.ui.theme.LightBackground
import com.example.jetchatai.ui.theme.SolakColor
import com.example.jetchatai.ui.theme.mainColor
import com.example.jetchatai.ui.theme.textColor
import com.google.firebase.*
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = LightBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(24.dp))
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.padding(32.dp)
        )

        Text(
            text = "Welcome Back!",
            fontFamily = jakarta_bold,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Enter your login details",
            fontFamily = jakarta_regular,
            fontSize = 14.sp,
            color = SolakColor
        )

        if(error != null) {
            Text(
                text = error!!,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Column(
            modifier = Modifier.padding(top = 24.dp)
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = {email = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text(text = "Enter Email")},
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Mail, contentDescription = "null")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedLeadingIconColor = mainColor,
                    unfocusedLeadingIconColor = Color.Gray,
                    focusedLabelColor = mainColor,
                    unfocusedLabelColor = Color.Gray,

                )

            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {password = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text(text = "Enter Password")},

                visualTransformation = if(isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },

                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

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

                    IconButton(onClick = {isPasswordVisible = !isPasswordVisible}) {
                        Icon(
                            imageVector = iconImage,
                            contentDescription = description
                        )
                    }

                },

                colors = OutlinedTextFieldDefaults.colors(

                    focusedLeadingIconColor = mainColor,
                    unfocusedLeadingIconColor = Color.Gray,
                    focusedLabelColor = mainColor,
                    unfocusedLabelColor = Color.Gray,
                ),

            )
            Spacer(modifier = Modifier.height(16.dp))
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
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = mainColor,
                )
            ) {
                if(loading) {
                    androidx.compose.material3.CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp), 
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color.LightGray,
                    thickness = 1.dp
                )
                
                Text(
                    text = "Or",
                    color = Color.Gray,
                    fontFamily = jakarta_regular,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color.LightGray,
                    thickness = 1.dp
                )
            }


            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.LightGray
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.google),
                        contentDescription = "Google logo",
                        modifier = Modifier.size(24.dp)
                        )
                    Spacer(modifier = Modifier.size(12.dp))
                }
                Text(
                    text = "Login with Google",
                    fontFamily = jakarta_regular,
                )

            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
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
                    modifier = Modifier.clickable(onClick = {
                        navController.navigate("register")
                    }),
                    fontFamily = jakarta_regular,
                    fontSize = 12.sp
                )
            }

        }


    }

}
//
//@Composable
//@Preview
//fun LoginScreenPreview() {
//    val context = LocalContext.current
//    val fakeNavController = rememberNavController()
//    LoginScreen(
//        navController = fakeNavController
//    )
//}