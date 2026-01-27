package io.kamel.image.decoder

import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.seiko.avif.AvifDecoder
import com.seiko.avif.createPlatformBitmap
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.ktor.utils.io.*
import kotlin.reflect.KClass

private const val Offset = 0

internal actual val ImageBitmapDecoder = object : Decoder<ImageBitmap> {

    override val outputKClass: KClass<ImageBitmap> = ImageBitmap::class

    override suspend fun decode(channel: ByteReadChannel, resourceConfig: ResourceConfig): ImageBitmap {
        val bytes = channel.toByteArray()

        val bitmap = if (Build.VERSION.SDK_INT < 31 && AvifDecoder.isAvifImage(bytes)) {
            // For API < 31, use AvifDecoder for AVIF images since BitmapFactory doesn't support AVIF
            try {
                val decoder = AvifDecoder.create(bytes)
                decoder.nextFrame()
                val frame = decoder.getFrame()
                val platformBitmap = frame.createPlatformBitmap()
                frame.decodeFrame(platformBitmap)
                platformBitmap
            } catch (t: Throwable) {
                throw IllegalArgumentException(
                    "Failed to decode ${bytes.size} bytes to a bitmap. Decoded bytes:\n${bytes.decodeToString()}\n",
                    t
                )
            }
        } else {
            // For API >= 31 or non-AVIF images, use BitmapFactory
            BitmapFactory.decodeByteArray(bytes, Offset, bytes.size)
                ?: throw IllegalArgumentException("Failed to decode ${bytes.size} bytes to a bitmap. Decoded bytes:\n${bytes.decodeToString()}\n")
        }

        return bitmap.asImageBitmap()
    }

}