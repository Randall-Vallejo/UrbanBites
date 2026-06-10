package com.ucb.app.home.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun ImagePicker(
    modifier: Modifier = Modifier,
    onImageSelected: (ByteArray) -> Unit,
    isUploading: Boolean,
    currentImageUrl: String
)
