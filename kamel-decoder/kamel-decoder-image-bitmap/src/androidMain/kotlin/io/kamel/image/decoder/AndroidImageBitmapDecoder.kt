package io.kamel.image.decoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlin.math.min
import kotlin.reflect.KClass

private const val Offset = 0

internal actual object ImageBitmapDecoder : Decoder<ImageBitmap> {

    override val outputKClass: KClass<ImageBitmap> = ImageBitmap::class

    override suspend fun decode(channel: ByteReadChannel, resourceConfig: ResourceConfig): ImageBitmap {
        val bytes = channel.toByteArray()
        val opt = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeByteArray(bytes, Offset, bytes.size, opt)
            ?: throw IllegalArgumentException("Failed to decode ${bytes.size} bytes to a bitmap. Decoded bytes:\n${bytes.decodeToString()}\n")
        val width = min(bitmap.width, resourceConfig.maxBitmapDecodeSize.width)
        val height = min(bitmap.height, resourceConfig.maxBitmapDecodeSize.height)
        val minScale = min(width / bitmap.width.toFloat(), height / bitmap.height.toFloat())
        return if (minScale < 1) {
            return Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width * minScale).toInt(),
                (bitmap.height * minScale).toInt(),
                true
            ).asImageBitmap()
        } else {
            bitmap.asImageBitmap()
        }
    }

}