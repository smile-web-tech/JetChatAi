package com.example.jetchatai

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jetchatai.ui.theme.LightBackground
import com.example.jetchatai.ui.theme.mainColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel = AuthViewModel()
) {
    val context = LocalContext.current
    val error by viewModel.errorMessage
    val loading by viewModel.isLoading
    var password by remember() {mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val isFormValid = name.isNotEmpty() && email.isNotEmpty() && password.length >= 6

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("")},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = "Back"
                        )
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightBackground
                )
            )
        }
    ) { innerPadding ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LightBackground)
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

Column(
    modifier = Modifier.padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {


    Image(
        painter = painterResource(R.drawable.logo),
        contentDescription = "Logo",
        modifier = Modifier.padding(32.dp)
    )
    Text(
        text = "Sign Up For Free!",
        fontFamily = jakarta_bold,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp
    )
    Spacer(modifier = Modifier.height(16.dp))


    OutlinedTextField(
        value = name,
        onValueChange = { name = it },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        placeholder = { Text(text = "Enter Your Name") },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Person, contentDescription = "null")
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
        value = email,
        onValueChange = { email = it },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        placeholder = { Text(text = "Enter Email") },
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
        onValueChange = { password = it },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        placeholder = { Text(text = "Enter Password") },

        visualTransformation = if (isPasswordVisible) {
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

            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
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
            viewModel.signUpWithCode(name, email, password) {
                navController.navigate("verif_screen/$email")
            }
        },
        enabled = !loading && isFormValid,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = mainColor,
        )
    ) {
        if(loading) {
            CircularProgressIndicator(
                color = mainColor,
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = "Sign Up",
                fontSize = 16.sp,
                fontFamily = jakarta_regular
            )
        }
    }
}
    }
    }
}

@Composable
@Preview
fun RegisterScreenPreview() {
    val fakeNavController = rememberNavController()
    RegisterScreen(
        navController = fakeNavController
    )
}