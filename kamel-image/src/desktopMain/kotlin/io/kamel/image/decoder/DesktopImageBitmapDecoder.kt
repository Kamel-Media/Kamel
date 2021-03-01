package io.kamel.image.decoder

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.ktor.util.*
import io.ktor.utils.io.*
import org.jetbrains.skija.Image

/**
 * Decodes and transfers [ByteReadChannel] to [ImageBitmap] using Skija [Image].
 */
internal actual object ImageBitmapDecoder : Decoder<ImageBitmap> {

    override suspend fun decode(channel: ByteReadChannel, resourceConfig: ResourceConfig): ImageBitmap {
        return Image.makeFromEncoded(channel.toByteArray())
            .asImageBitmap()
    }

}