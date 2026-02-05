package com.example.jetchatai.screens

import android.R.attr.padding
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jetchatai.R
import com.example.jetchatai.jakarta_bold
import com.example.jetchatai.jakarta_regular
import com.example.jetchatai.ui.theme.mainColor
import com.example.jetchatai.viewmodels.ChatViewModel
import com.example.jetchatai.viewmodels.MessageModel
import com.mikepenz.markdown.m3.Markdown // Standard for 2026 M3 projects
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.rememberMarkdownState
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = viewModel()
) {

    val messages = viewModel.messageList


    val speechLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = data?.get(0) ?: ""
            if (spokenText.isNotBlank()) {
                viewModel.sendMessage(spokenText) // Directly use your ViewModel!
            }
        }
    }
    val onVoiceClick = {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
        }
        speechLauncher.launch(intent)
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
                            "JetChat",
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
        bottomBar = {
            Column {
                ChatInput(onMessageSent = { text ->
                    viewModel.sendMessage(text)
                },
                    onVoiceClick = { onVoiceClick() }
                    )
                BottomAppBar(navController)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            reverseLayout = true // Chat starts from bottom
        ) {
            if (viewModel.isLoading) {
                item { ThinkingBubble() }
            }
            items(messages.reversed()) { msg ->
                ChatBubble(msg)
            }
        }
    }
}

@Composable
@Preview
fun ChatScreenPreview(){
    var fakeNavController = rememberNavController()
    ChatScreen(
        navController = fakeNavController
    )
}
@Composable
fun ChatBubble(message: MessageModel) {
    val isUser = message.role == "user"
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isUser) mainColor else Color(0xFFF1F1F1),
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isUser) 16.dp else 0.dp,
                        bottomEnd = if (isUser) 0.dp else 16.dp
                    )
                )
                .padding(12.dp)
        ) {
            Column {
                if (isUser) {
                    Text(
                        text = message.text,
                        color = Color.White,
                        fontFamily = jakarta_regular,
                        fontSize = 15.sp
                    )
                } else {

                    Markdown(
                        content = message.text,       // Explicitly name 'content'
                        colors = markdownColor(
                            text = Color.Black,
                            codeBackground = Color(0xFFE0E0E0)
                        ),
                        typography = markdownTypography(
                            paragraph = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 15.sp,
                                fontFamily = jakarta_regular
                            )
                        ),
                        modifier = Modifier.padding(4.dp) // Explicitly name 'modifier'
                    )
                }

                if (!isUser) {
                    Spacer(modifier = Modifier.height(8.dp))
                    IconButton(
                        onClick = {
                            scope.launch {
                                clipboard.setClipEntry(
                                    ClipEntry(ClipData.newPlainText("AI response", message.text))
                                )
                            }
                        },
                        modifier = Modifier.align(Alignment.End).size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.copy),
                            contentDescription = "Copy text",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatInput(
    onMessageSent: (String) -> Unit,
    onVoiceClick: () -> Unit
) {
    var userText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. The Modern Text Field
        OutlinedTextField(
            value = userText,
            onValueChange = { userText = it },
            modifier = Modifier
                .weight(1f)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp)) // Modern Shadow
                .background(Color.White, RoundedCornerShape(12.dp)),
            placeholder = { Text("Ask JetChat", color = Color.Gray) },
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Transparent, // Hides the border for a cleaner look
                unfocusedBorderColor = Color.Transparent
            ),
            trailingIcon = {
                IconButton(onClick =
                    {onVoiceClick()
                    } ) {
                Icon(
                    painter = painterResource(id = R.drawable.mic),
                    contentDescription = "Voice",
                    tint = mainColor,
                    modifier = Modifier.size(24.dp)
                )
            }}
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 2. The Circular Send Button
        Box(
            modifier = Modifier
                .size(56.dp)
                .shadow(elevation = 10.dp, shape = CircleShape) // Floating effect
                .background(mainColor, CircleShape)
                .clickable {
                    if (userText.isNotBlank()) {
                        onMessageSent(userText)
                        userText = ""
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}


@Composable
fun ThinkingBubble() {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")

    val dots = listOf(0, 1, 2).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = -10f, // How high they jump
            animationSpec = infiniteRepeatable(
                animation = tween(600, delayMillis = index * 150, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "dot_$index"
        )
    }

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color(0xFFF1F1F1), RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        dots.forEach { dotOffset ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .size(8.dp)
                    .graphicsLayer(translationY = dotOffset.value) // Apply the jump
                    .background(Color.Gray, CircleShape)
            )
        }
    }
}