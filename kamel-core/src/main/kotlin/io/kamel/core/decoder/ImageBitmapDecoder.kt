package io.kamel.core.decoder

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import io.kamel.core.Resource
import io.kamel.core.utils.tryCatching
import io.ktor.util.*
import io.ktor.utils.io.*
import org.jetbrains.skija.Image as SkijaImage

/**
 * Decodes and transfers [ByteReadChannel] to [ImageBitmap] using [SkijaImage].
 */
internal object ImageBitmapDecoder : Decoder<ImageBitmap> {

    override suspend fun decode(channel: ByteReadChannel): Result<ImageBitmap> = runCatching {
        SkijaImage
            .makeFromEncoded(channel.toByteArray())
            .asImageBitmap()
    }

    override suspend fun decodeResource(channel: ByteReadChannel): Resource<ImageBitmap> = tryCatching {
        SkijaImage
            .makeFromEncoded(channel.toByteArray())
            .asImageBitmap()
    }

}