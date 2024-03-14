package io.kamel.core;

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

public interface AnimatedImage {
    @Composable
    public fun animate(): ImageBitmap
}