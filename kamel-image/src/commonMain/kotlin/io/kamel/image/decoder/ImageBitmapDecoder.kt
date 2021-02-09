package io.kamel.image.decoder

import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.decoder.Decoder
import io.ktor.utils.io.*

/**
 * Decodes and transfers [ByteReadChannel] to [ImageBitmap].
 */
internal expect object ImageBitmapDecoder : Decoder<ImageBitmap>