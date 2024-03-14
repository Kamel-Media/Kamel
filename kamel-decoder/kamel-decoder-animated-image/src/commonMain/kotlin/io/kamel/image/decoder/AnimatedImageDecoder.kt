package io.kamel.image.decoder

import io.kamel.core.AnimatedImage
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.ktor.utils.io.*
import kotlin.reflect.KClass

/**
 * Decodes and transfers [ByteReadChannel] to [AnimatedImage] using Skia [Image].
 */
internal expect object AnimatedImageDecoder : Decoder<AnimatedImage> {
    override val outputKClass: KClass<AnimatedImage>
    override suspend fun decode(
        channel: ByteReadChannel,
        resourceConfig: ResourceConfig
    ): AnimatedImage
}