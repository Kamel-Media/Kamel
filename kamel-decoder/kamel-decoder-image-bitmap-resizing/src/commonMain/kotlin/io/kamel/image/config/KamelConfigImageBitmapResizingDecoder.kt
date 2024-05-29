package io.kamel.image.config

import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.config.KamelConfigBuilder
import io.kamel.image.decoder.ImageBitmapResizingDecoder

/**
 * Adds an [ImageBitmap] decoder to the [KamelConfigBuilder].
 */
public fun KamelConfigBuilder.imageBitmapResizingDecoder(): Unit = decoder(ImageBitmapResizingDecoder)
