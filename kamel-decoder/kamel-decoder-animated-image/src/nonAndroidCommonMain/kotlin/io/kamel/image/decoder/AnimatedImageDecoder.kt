package io.kamel.image.decoder

import io.kamel.core.AnimatedImage
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.ktor.util.*
import io.ktor.utils.io.*
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data
import org.jetbrains.skia.Image
import kotlin.reflect.KClass


/**
 * Decodes and transfers [ByteReadChannel] to [AnimatedImage] using Skia [Image].
 */
actual internal object AnimatedImageDecoder : Decoder<AnimatedImage> {

    actual override val outputKClass: KClass<AnimatedImage> = AnimatedImage::class

    actual override suspend fun decode(
        channel: ByteReadChannel, resourceConfig: ResourceConfig
    ): AnimatedImage {
        val bytes = channel.toByteArray()
        return try {
            val data = Data.makeFromBytes(bytes)
            val codec = Codec.makeFromData(data)
            AnimatedImageImpl(codec)
        } catch (t: Throwable) {
            throw throw IllegalArgumentException(
                "Failed to decode ${bytes.size} bytes to a bitmap. Decoded bytes:\n${
                    bytes.slice(0 until 1024).toByteArray().decodeToString()
                }\n",
                t
            )
        }
    }
}