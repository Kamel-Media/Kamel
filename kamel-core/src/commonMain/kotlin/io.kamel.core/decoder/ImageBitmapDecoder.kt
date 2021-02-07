package io.kamel.core.decoder

import androidx.compose.ui.graphics.ImageBitmap
import io.ktor.utils.io.*

/**
 * Decodes and transfers [ByteReadChannel] to [ImageBitmap].
 */
internal expect object ImageBitmapDecoder : Decoder<ImageBitmap>