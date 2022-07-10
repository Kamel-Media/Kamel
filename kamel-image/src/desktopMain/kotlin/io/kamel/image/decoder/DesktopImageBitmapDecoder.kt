package io.kamel.image.decoder

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import io.kamel.core.config.ResourceConfig
import io.kamel.core.decoder.Decoder
import io.ktor.util.*
import io.ktor.utils.io.*
import org.jetbrains.skia.Image

/**
 * Decodes and transfers [ByteReadChannel] to [ImageBitmap] using Skia [Image].
 */
internal actual object ImageBitmapDecoder : Decoder<ImageBitmap> {

    override suspend fun decode(
        channel: ByteReadChannel,
        resourceConfig: ResourceConfig
    ): ImageBitmap = Image.makeFromEncoded(channel.toByteArray()).toComposeImageBitmap()

}