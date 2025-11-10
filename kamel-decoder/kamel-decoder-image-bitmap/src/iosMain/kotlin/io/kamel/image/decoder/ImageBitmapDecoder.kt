package io.kamel.image.decoder

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.seiko.avif.AvifDecoder
import com.seiko.avif.createPlatformBitmap
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
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
            if (!AvifDecoder.isAvifImage(bytes)) {
                return Image.makeFromEncoded(bytes).toComposeImageBitmap()
            }
            val decoder = AvifDecoder.create(bytes)
            decoder.nextFrame()
            val frame = decoder.getFrame()
            val bitmap = frame.createPlatformBitmap()
            frame.decodeFrame(bitmap)
            return bitmap.asComposeImageBitmap()
        } catch (t: Throwable) {
            throw throw IllegalArgumentException("Failed to decode ${bytes.size} bytes to a bitmap. Decoded bytes:\n${bytes.decodeToString()}\n")
        }
    }

}