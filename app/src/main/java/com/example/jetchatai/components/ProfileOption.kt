package com.example.jetchatai.models

import androidx.compose.ui.graphics.vector.ImageVector

data class ProfileOption(
    val icon: ImageVector,
    val title: String,
    val onClick: () -> Unit
)