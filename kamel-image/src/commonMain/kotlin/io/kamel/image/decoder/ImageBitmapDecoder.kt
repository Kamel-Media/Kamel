package io.kamel.image.decoder

import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.config.*
import io.kamel.core.decoder.Decoder
import io.ktor.client.features.logging.*

/**
 * Decodes and transfers [ByteReadChannel] to [ImageBitmap].
 */
internal expect object ImageBitmapDecoder : Decoder<ImageBitmap>

public fun KamelConfigBuilder.imageBitmapDecoder(): Unit = decoder(ImageBitmapDecoder)