package io.kamel.image.decoder

import io.kamel.core.AnimatedImage
import io.kamel.core.decoder.Decoder
import io.ktor.utils.io.*

/**
 * Decodes and transfers [ByteReadChannel] to [AnimatedImage] using Skia [Image].
 */
internal expect val AnimatedImageDecoder: Decoder<AnimatedImage>