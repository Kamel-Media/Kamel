package io.kamel.image.decoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.ktor.util.*
import io.ktor.utils.io.*

private const val Offset = 0

internal actual object ImageBitmapDecoder : Decoder<ImageBitmap> {

    override suspend fun decode(channel: ByteReadChannel, resourceConfig: ResourceConfig): ImageBitmap {
        val bytes = channel.toByteArray()
        val bitmap = BitmapFactory.decodeByteArray(bytes, Offset, bytes.size) as Bitmap
        return bitmap.asImageBitmap()
    }

}