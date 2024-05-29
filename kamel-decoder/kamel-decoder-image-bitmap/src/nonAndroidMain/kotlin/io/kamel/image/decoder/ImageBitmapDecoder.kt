package io.kamel.image.decoder

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.ktor.util.*
import io.ktor.utils.io.*
import org.jetbrains.skia.Image
import kotlin.reflect.KClass

/**
 * Decodes and transfers [ByteReadChannel] to [ImageBitmap] using Skia [Image].
 */
internal actual val ImageBitmapDecoder = object : Decoder<ImageBitmap> {

    override val outputKClass: KClass<ImageBitmap> = ImageBitmap::class

    override suspend fun decode(
        channel: ByteReadChannel,
        resourceConfig: ResourceConfig
    ): ImageBitmap {
        val bytes = channel.toByteArray()
        return try {
            Image.makeFromEncoded(bytes).toComposeImageBitmap()
        } catch (t: Throwable) {
            throw throw IllegalArgumentException("Failed to decode ${bytes.size} bytes to a bitmap. Decoded bytes:\n${bytes.decodeToString()}\n")
        }
    }

}