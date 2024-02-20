package io.kamel.image.config

import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.config.KamelConfigBuilder
import io.kamel.image.decoder.ImageBitmapDecoder

/**
 * Adds an [ImageBitmap] decoder to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.imageBitmapDecoder(): Unit = decoder(ImageBitmapDecoder)
