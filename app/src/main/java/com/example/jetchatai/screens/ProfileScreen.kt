package com.example.jetchatai.screens

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jetchatai.jakarta_bold
import com.example.jetchatai.jakarta_regular
import com.example.jetchatai.models.ProfileOption
import com.example.jetchatai.ui.theme.mainColor
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    var profileName by remember { mutableStateOf("Loading...") }
    var profileEmail by remember { mutableStateOf("Loading...") }


    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        profileName = document.getString("name") ?: "No Name"
                        profileEmail = document.getString("email") ?: "No Email"
                    }
                }
                .addOnFailureListener {
                    profileEmail = "Error loading"
                }
        }
    }

    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 4.dp,
                color = Color.White
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Profile",
                            fontFamily = jakarta_bold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        },
        bottomBar = { BottomAppBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "User profile",
                modifier = Modifier.size(106.dp)
            )
                Text(
                    text = "Hi! $profileName",
                    fontFamily = jakarta_bold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            Text(
                text = profileEmail,
                fontFamily = jakarta_regular,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.size(24.dp))
            val options = listOf(
                ProfileOption(Icons.Default.Settings, "Settings") { /* Navigate to Settings */ },
                ProfileOption(Icons.Default.Notifications, "Notifications") { /* Navigate to Notifications */ },
                ProfileOption(Icons.Default.Help, "Support and Help") { /* Navigate to Help */ },
                ProfileOption(Icons.Default.ExitToApp, "Logout") {
                    com.google.firebase.Firebase.auth.signOut()
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                }
            )

            androidx.compose.foundation.lazy.LazyColumn {
                items(options) { item ->
                    ProfileOptionRow(option = item)
                }
            }
        }


    }

}

@Composable
@Preview
fun ProfileScreen(){
    var fakeNavController = rememberNavController()
    ChatScreen(
        navController = fakeNavController
    )
}


@Composable
fun ProfileOptionRow(option: com.example.jetchatai.models.ProfileOption) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .clickable { option.onClick() },
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = option.icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.DarkGray
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = option.title,
                    fontFamily = jakarta_regular,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.LightGray
            )
        }
    }
}