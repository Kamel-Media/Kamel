package io.kamel.image.decoder

import androidx.compose.ui.graphics.painter.Painter
import io.kamel.core.decoder.Decoder

/**
 * Decodes and transfers [ByteReadChannel] to [Painter].
 */
internal expect class SvgDecoder() : Decoder<Painter>