package io.kamel.image.decoder

import androidx.compose.ui.graphics.ImageBitmap
import io.kamel.core.decoder.Decoder

/**
 * Decodes and transfers [io.ktor.utils.io.ByteReadChannel] to [ImageBitmap].
 */
internal expect val ImageBitmapResizingDecoder: Decoder<ImageBitmap>