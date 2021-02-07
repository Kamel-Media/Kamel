package io.kamel.core.decoder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import io.kamel.core.ExperimentalKamelApi
import io.kamel.core.Resource
import io.kamel.core.utils.tryCatching
import io.ktor.util.*
import io.ktor.utils.io.*

private const val Offset = 0

internal actual object ImageBitmapDecoder : Decoder<ImageBitmap> {
    override suspend fun decode(channel: ByteReadChannel): Result<ImageBitmap> = runCatching {
        val bytes = channel.toByteArray()
        val bitmap = BitmapFactory.decodeByteArray(bytes, Offset, bytes.size) as Bitmap
        bitmap.asImageBitmap()
    }

    @ExperimentalKamelApi
    override suspend fun decodeResource(channel: ByteReadChannel): Resource<ImageBitmap> = tryCatching {
        val bytes = channel.toByteArray()
        val bitmap = BitmapFactory.decodeByteArray(bytes, Offset, bytes.size) as Bitmap
        bitmap.asImageBitmap()
    }
}