package io.kamel.image.decoder

import androidx.compose.ui.graphics.vector.ImageVector
import io.kamel.core.decoder.Decoder

/**
 * Decodes and transfers [ByteReadChannel] to [ImageVector].
 */
internal expect val ImageVectorDecoder : Decoder<ImageVector>