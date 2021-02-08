package io.kamel.core.decoder

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import io.kamel.core.ExperimentalKamelApi
import io.kamel.core.Resource
import io.kamel.core.utils.tryCatching
import io.ktor.util.*
import io.ktor.utils.io.*
import org.jetbrains.skija.Image

/**
 * Decodes and transfers [ByteReadChannel] to [ImageBitmap] using Skija [Image].
 */
internal actual object ImageBitmapDecoder : Decoder<ImageBitmap> {

    override suspend fun decode(channel: ByteReadChannel): Result<ImageBitmap> = runCatching {
        Image.makeFromEncoded(channel.toByteArray())
            .asImageBitmap()
    }

    @ExperimentalKamelApi
    override suspend fun decodeResource(channel: ByteReadChannel): Resource<ImageBitmap> = tryCatching {
        Image.makeFromEncoded(channel.toByteArray())
            .asImageBitmap()
    }

}