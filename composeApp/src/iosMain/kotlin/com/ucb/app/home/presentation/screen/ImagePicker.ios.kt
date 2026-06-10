package com.ucb.app.home.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
actual fun ImagePicker(
    modifier: Modifier,
    onImageSelected: (ByteArray) -> Unit,
    isUploading: Boolean,
    currentImageUrl: String
) {
    // Placeholder para iOS para evitar error de compilación
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text("Image Picker no implementado en iOS")
    }
}
